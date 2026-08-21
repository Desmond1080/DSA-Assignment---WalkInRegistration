/**
 * Author: Law Tian Xiang
 * 
 */
package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import Entity.Member;
import Entity.RedemptionRequest;

public class LoyaltyAndRewardsControl {
    
    private QueueInterface<RedemptionRequest> redemptionQueue = new ArrayQueue<>();
    
    // dummy data
    private Member[] memberDatabase = {
        new Member("M001", "Alice Gold", 6500, "Gold"),
        new Member("M002", "Bob Gold", 5200, "Gold"),
        new Member("M003", "Charlie Gold", 8000, "Gold"),
        
        new Member("M004", "Dave Silver", 3500, "Silver"),
        new Member("M005", "Eve Silver", 2100, "Silver"),
        new Member("M006", "Frank Silver", 4900, "Silver"),
        
        new Member("M007", "Grace Bronze", 1500, "Bronze"),
        new Member("M008", "Heidi Bronze", 500, "Bronze"),
        new Member("M009", "Ivan Bronze", 0, "Bronze")
    };
    
    // view all member profile 
    public String getAllMemberProfiles() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------------------\n");
        sb.append(String.format("%-10s | %-20s | %-10s | %-10s\n", "Member ID", "Name", "Tier", "Points"));
        sb.append("--------------------------------------------------------------\n");
        for (Member m : memberDatabase) {
            sb.append(String.format("%-10s | %-20s | %-10s | %-10d\n", 
                    m.getMemberId(), m.getName(), m.getTier(), m.getPoints()));
        }
        sb.append("--------------------------------------------------------------");
        return sb.toString();
    }
    
    // search member based on member id 
    public Member findMember(String memberId) {
        for (Member m : memberDatabase) {
            if (m.getMemberId().equalsIgnoreCase(memberId)) {
                return m;
            }
        }
        return null;
    }

    // retrieve member point 
    public int getMemberPoints(String memberId) {
        Member m = findMember(memberId);
        return (m != null) ? m.getPoints() : -1; 
    }
    
    // add member point 
    public boolean addPointsToMember(String memberId, int pointsToAdd) {
        if (pointsToAdd <= 0) return false;
        Member member = findMember(memberId);
        if (member != null) {
            member.setPoints(member.getPoints() + pointsToAdd);
            return true;
        }
        return false;
    }
    
    // request reward redemption 
    public boolean requestReward(String memberId, String rewardItem, int cost) {
        Member member = findMember(memberId);
        if (member != null && member.getPoints() >= cost) {
            RedemptionRequest newRequest = new RedemptionRequest(member, rewardItem, cost);
            
            redemptionQueue.enqueue(newRequest);
            return true;
        }
        return false;
    }
    
    // check has next request or not 
    public String peekNextRequestDetails() {
        if (redemptionQueue.isEmpty()) {
            return null;
        }
        
        RedemptionRequest request = redemptionQueue.getFront();
        return String.format("Member: %s (ID: %s) | Reward: %s | Cost: %d points", 
                request.getMember().getName(), request.getMember().getMemberId(), 
                request.getRewardItem(), request.getPointsCost());
    }
    
    // process next redemption request 
    public String processNextRedemption() {
        if (redemptionQueue.isEmpty()) return "No pending redemptions.";
        
        RedemptionRequest request = redemptionQueue.dequeue();
        Member member = request.getMember();
        
        if (member.deductPoints(request.getPointsCost())) {
            return "Successfully processed! Remaining points: " + member.getPoints();
        } else {
            return "Failed to process! Insufficient points.";
        }
    }
    
    // pending request counter 
    public int getPendingRequestsCount() {
        return redemptionQueue.getNumberOfEntries();
    }

    // report generation module
    public String generatePointsRankingReport(int minPoints) {
        Member[] filtered = new Member[memberDatabase.length];
        int count = 0;

        for (Member m : memberDatabase) {
            if (m.getPoints() >= minPoints) {
                filtered[count++] = m;
            }
        }

        // sorting point from high to low 
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

        StringBuilder report = new StringBuilder();
        report.append("\n================================================================================\n");
        report.append("               TARUMT RESORT - LOYALTY & REWARDS MODULE\n");
        report.append("                    MEMBER POINT RANKING REPORT\n");
        report.append("================================================================================\n");
        report.append(String.format("Filter Criteria: Points >= %d\n", minPoints));
        report.append("--------------------------------------------------------------------------------\n");
        
        report.append(String.format("%-6s | %-10s | %-20s | %-10s | %-10s\n", "Rank", "Member ID", "Member Name", "Tier", "Points"));
        report.append("--------------------------------------------------------------------------------\n");

        if (count == 0) {
            report.append("No members matched the filter criteria.\n");
        } else {
            for (int i = 0; i < count; i++) {
                report.append(String.format("%-6d | %-10s | %-20s | %-10s | %-10d\n", 
                    (i + 1), filtered[i].getMemberId(), filtered[i].getName(), 
                    filtered[i].getTier(), filtered[i].getPoints()));
            }
        }
        report.append("--------------------------------------------------------------------------------\n");
        report.append("Total Members Found: ").append(count).append("\n");
        report.append("================================================================================\n");
        
        return report.toString();
    }

    public String generateTierRosterReport(String targetTier) {
        Member[] filtered = new Member[memberDatabase.length];
        int count = 0;

        // searching and filtering 
        for (Member m : memberDatabase) {
            if (m.getTier().equalsIgnoreCase(targetTier) && m.getPoints() > 0) {
                filtered[count++] = m;
            }
        }

        // sorting from high to low
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (filtered[j].getPoints() < filtered[j + 1].getPoints()) {
                    Member temp = filtered[j];
                    filtered[j] = filtered[j + 1];
                    filtered[j + 1] = temp;
                }
            }
        }

        // output format
        StringBuilder report = new StringBuilder();
        report.append("\n================================================================================\n");
        report.append("               TARUMT RESORT - LOYALTY & REWARDS MODULE\n");
        report.append("                    TIER SPECIFIC RANKING REPORT\n");
        report.append("================================================================================\n");
        report.append(String.format("Filter Criteria: Tier == %s AND Points > 0\n", targetTier));
        report.append("--------------------------------------------------------------------------------\n");
        
        report.append(String.format("%-6s | %-20s | %-10s | %-10s\n", "Rank", "Member Name", "Member ID", "Points"));
        report.append("--------------------------------------------------------------------------------\n");

        if (count == 0) {
            report.append("No active members found in this tier.\n");
        } else {
            for (int i = 0; i < count; i++) {
                report.append(String.format("%-6d | %-20s | %-10s | %-10d\n", 
                    (i + 1), filtered[i].getName(), filtered[i].getMemberId(), filtered[i].getPoints()));
            }
        }
        report.append("--------------------------------------------------------------------------------\n");
        report.append("Total Active Members: ").append(count).append("\n");
        report.append("================================================================================\n");
        
        return report.toString();
    }
}