/**
 * Author: Law Tian Xiang
 * Description: Control class managing business logic and Report Generation
 */
package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import Entity.Member;
import Entity.RedemptionRequest;

public class LoyaltyAndRewardsControl {
    
    // YELLOW HIGHLIGHT: Declaration and creation of collection ADT object
    private QueueInterface<RedemptionRequest> redemptionQueue = new ArrayQueue<>();
    
    // Hardcoded member database
    private Member[] memberDatabase = {
        new Member("M001", "John Doe", 1500, "Elite"),
        new Member("M002", "Jane Smith", 300, "Standard"),
        new Member("M003", "Law Tian Xiang", 4500, "Diamond"),
        new Member("M004", "Alice Wonders", 6000, "Platinum"),
        new Member("M005", "Bob Miller", 0, "Standard")
    };

    public Member findMember(String memberId) {
        for (Member m : memberDatabase) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return m;
            }
        }
        return null;
    }

    public boolean addPointsToMember(String memberId, int pointsToAdd) {
        if (pointsToAdd <= 0) return false;
        Member member = findMember(memberId);
        if (member != null) {
            member.setPoints(member.getPoints() + pointsToAdd);
            return true;
        }
        return false;
    }

    public boolean requestReward(String memberId, String rewardItem, int cost) {
        Member member = findMember(memberId);
        if (member != null && member.getPoints() >= cost) {
            RedemptionRequest newRequest = new RedemptionRequest(member, rewardItem, cost);
            // YELLOW HIGHLIGHT: Invocation of collection ADT method
            redemptionQueue.enqueue(newRequest);
            return true;
        }
        return false;
    }

    // NEW: Safely looks at the front of the queue without deleting it
    public String peekNextRequestDetails() {
        // YELLOW HIGHLIGHT: Invocation of collection ADT method
        if (redemptionQueue.isEmpty()) {
            return null;
        }
        // YELLOW HIGHLIGHT: Invocation of collection ADT method
        RedemptionRequest request = redemptionQueue.getFront();
        return String.format("Member: %s (ID: %s) | Reward: %s | Cost: %d points", 
                request.getMember().getName(), request.getMember().getMemberId(), 
                request.getRewardItem(), request.getPointsCost());
    }

    public String processNextRedemption() {
        // YELLOW HIGHLIGHT: Invocation of collection ADT method
        if (redemptionQueue.isEmpty()) return "No pending redemptions.";
        
        // YELLOW HIGHLIGHT: Invocation of collection ADT method
        RedemptionRequest request = redemptionQueue.dequeue();
        Member member = request.getMember();
        
        if (member.deductPoints(request.getPointsCost())) {
            return "Successfully processed! Remaining points: " + member.getPoints();
        } else {
            return "Failed to process! Insufficient points.";
        }
    }
    
    public int getPendingRequestsCount() {
        // YELLOW HIGHLIGHT: Invocation of collection ADT method
        return redemptionQueue.getNumberOfEntries();
    }

    // ==========================================================
    // REPORT GENERATION MODULE
    // ==========================================================

    /**
     * REPORT 1: High-Value Member Ranking
     * Filters by min points and excluded tier (Multiple Criteria).
     * Sorts descending by points using a Custom Selection Sort.
     */
    /**
     * REPORT 1: High-Value Member Ranking
     * Filters by min points.
     * Sorts descending by points using a Custom Selection Sort.
     */
    public String generatePointsRankingReport(int minPoints) {
        Member[] filtered = new Member[memberDatabase.length];
        int count = 0;

        // 1. Searching & Filtering (Single Criterion)
        for (Member m : memberDatabase) {
            if (m.getPoints() >= minPoints) {
                filtered[count++] = m;
            }
        }

        // 2. Custom Selection Sort (Highest Points to Lowest)
        for (int i = 0; i < count - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < count; j++) {
                if (filtered[j].getPoints() > filtered[maxIndex].getPoints()) {
                    maxIndex = j;
                }
            }
            Member temp = filtered[i];
            filtered[i] = filtered[maxIndex];
            filtered[maxIndex] = temp;
        }

        // 3. Format Console Output
        StringBuilder report = new StringBuilder();
        report.append("\n======================================================================\n");
        report.append("             TARUMT RESORT - LOYALTY & REWARDS MODULE\n");
        report.append("                 HIGH-VALUE MEMBER RANKING REPORT\n");
        report.append("======================================================================\n");
        report.append(String.format("Filter Criteria: Points >= %d\n", minPoints));
        report.append("----------------------------------------------------------------------\n");
        report.append(String.format("%-10s | %-20s | %-12s | %-10s\n", "Member ID", "Member Name", "Tier", "Points"));
        report.append("----------------------------------------------------------------------\n");

        if (count == 0) {
            report.append("No members matched the filter criteria.\n");
        } else {
            for (int i = 0; i < count; i++) {
                report.append(String.format("%-10s | %-20s | %-12s | %-10d\n", 
                    filtered[i].getMemberId(), filtered[i].getName(), 
                    filtered[i].getTier(), filtered[i].getPoints()));
            }
        }
        report.append("----------------------------------------------------------------------\n");
        report.append("Total Members Found: ").append(count).append("\n");
        report.append("======================================================================\n");
        
        return report.toString();
    }
    /**
     * REPORT 2: Specific Tier Alphabetical Roster
     * Filters by exact tier and excludes members with 0 points.
     * Sorts alphabetically by name using a Custom Bubble Sort.
     */
    public String generateTierRosterReport(String targetTier) {
        Member[] filtered = new Member[memberDatabase.length];
        int count = 0;

        // 1. Searching & Filtering
        for (Member m : memberDatabase) {
            if (m.getTier().equalsIgnoreCase(targetTier) && m.getPoints() > 0) {
                filtered[count++] = m;
            }
        }

        // 2. Custom Bubble Sort (Alphabetical by Name)
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (filtered[j].getName().compareToIgnoreCase(filtered[j + 1].getName()) > 0) {
                    Member temp = filtered[j];
                    filtered[j] = filtered[j + 1];
                    filtered[j + 1] = temp;
                }
            }
        }

        // 3. Format Console Output
        StringBuilder report = new StringBuilder();
        report.append("\n======================================================================\n");
        report.append("             TARUMT RESORT - LOYALTY & REWARDS MODULE\n");
        report.append("                  TIER SPECIFIC ACTIVITY ROSTER\n");
        report.append("======================================================================\n");
        report.append(String.format("Filter Criteria: Tier == %s AND Points > 0\n", targetTier));
        report.append("----------------------------------------------------------------------\n");
        report.append(String.format("%-20s | %-10s | %-10s\n", "Member Name", "Member ID", "Points"));
        report.append("----------------------------------------------------------------------\n");

        if (count == 0) {
            report.append("No active members found in this tier.\n");
        } else {
            for (int i = 0; i < count; i++) {
                report.append(String.format("%-20s | %-10s | %-10d\n", 
                    filtered[i].getName(), filtered[i].getMemberId(), filtered[i].getPoints()));
            }
        }
        report.append("----------------------------------------------------------------------\n");
        report.append("Total Active Members: ").append(count).append("\n");
        report.append("======================================================================\n");
        
        return report.toString();
    }
}