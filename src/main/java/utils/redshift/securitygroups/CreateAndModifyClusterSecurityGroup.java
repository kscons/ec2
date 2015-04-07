package utils.redshift.securitygroups;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

        import com.amazonaws.auth.AWSCredentials;
        import com.amazonaws.auth.PropertiesCredentials;
        import com.amazonaws.auth.profile.ProfileCredentialsProvider;
        import com.amazonaws.services.redshift.AmazonRedshiftClient;
        import com.amazonaws.services.redshift.model.*;

public class CreateAndModifyClusterSecurityGroup {

    public static AmazonRedshiftClient client;
    public static String clusterSecurityGroupName = "redshift-testsecurity2";
    public static String clusterIdentifier = "logitech-analytics-ksolod";
    public static String ownerID = "655334590711";
    public static String CRIP = "0.0.0.0/0";

    public static void main(String[] args) throws IOException {

        AWSCredentials credentials =  new ProfileCredentialsProvider().getCredentials();

        client = new AmazonRedshiftClient(credentials);

        try {
            createClusterSecurityGroup();
            describeClusterSecurityGroups();
            addIngressRules();
            associateSecurityGroupWithCluster();
        } catch (Exception e) {
            System.err.println("Operation failed: " + e.getMessage());
        }
    }

    private static void createClusterSecurityGroup() {
        CreateClusterSecurityGroupRequest request = new CreateClusterSecurityGroupRequest()
                .withDescription("my cluster security group")
                .withClusterSecurityGroupName(clusterSecurityGroupName);

        client.createClusterSecurityGroup(request);
        System.out.format("Created cluster security group: '%s'\n", clusterSecurityGroupName);
    }

    private static void addIngressRules() {

        AuthorizeClusterSecurityGroupIngressRequest request = new AuthorizeClusterSecurityGroupIngressRequest()
                .withClusterSecurityGroupName(clusterSecurityGroupName)
                .withCIDRIP(CRIP);

        ClusterSecurityGroup result = client.authorizeClusterSecurityGroupIngress(request);

        request = new AuthorizeClusterSecurityGroupIngressRequest()
                .withClusterSecurityGroupName(clusterSecurityGroupName)
                .withEC2SecurityGroupName("default")
                .withEC2SecurityGroupOwnerId(ownerID);
        result = client.authorizeClusterSecurityGroupIngress(request);
        System.out.format("\nAdded ingress rules to security group '%s'\n", clusterSecurityGroupName);
        printResultSecurityGroup(result);
    }

    private static void associateSecurityGroupWithCluster() {

        // Get existing security groups used by the cluster.
        DescribeClustersRequest request = new DescribeClustersRequest()
                .withClusterIdentifier(clusterIdentifier);

        DescribeClustersResult result = client.describeClusters(request);
        List<ClusterSecurityGroupMembership> membershipList =
                result.getClusters().get(0).getClusterSecurityGroups();

        List<String> secGroupNames = new ArrayList<String>();
        for (ClusterSecurityGroupMembership mem : membershipList) {
            secGroupNames.add(mem.getClusterSecurityGroupName());
        }
        // Add new security group to the list.
        secGroupNames.add(clusterSecurityGroupName);

        // Apply the change to the cluster.
        ModifyClusterRequest request2 = new ModifyClusterRequest()
                .withClusterIdentifier(clusterIdentifier)
                .withClusterSecurityGroups(secGroupNames);

        Cluster result2 = client.modifyCluster(request2);
        System.out.format("\nAssociated security group '%s' to cluster '%s'.", clusterSecurityGroupName, clusterIdentifier);
    }

    private static void describeClusterSecurityGroups() {
        DescribeClusterSecurityGroupsRequest request = new DescribeClusterSecurityGroupsRequest();

        DescribeClusterSecurityGroupsResult result = client.describeClusterSecurityGroups(request);
        printResultSecurityGroups(result.getClusterSecurityGroups());
    }

    private static void printResultSecurityGroups(List<ClusterSecurityGroup> groups)
    {
        if (groups == null)
        {
            System.out.println("\nDescribe cluster security groups result is null.");
            return;
        }

        System.out.println("\nPrinting security group results:");
        for (ClusterSecurityGroup group : groups)
        {
            printResultSecurityGroup(group);
        }
    }
    private static void printResultSecurityGroup(final ClusterSecurityGroup group) {
        System.out.format("\nName: '%s', Description: '%s'\n", group.getClusterSecurityGroupName(), group.getDescription());
        for (EC2SecurityGroup g : group.getEC2SecurityGroups()) {
            System.out.format("EC2group: '%s', '%s', '%s'\n", g.getEC2SecurityGroupName(), g.getEC2SecurityGroupOwnerId(), g.getStatus());
        }
        for (IPRange range : group.getIPRanges()) {
            System.out.format("IPRanges: '%s', '%s'\n", range.getCIDRIP(), range.getStatus());

        }
    }
}

