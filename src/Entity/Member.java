/**
 * Author: Law Tian Xiang
 * 
 */
package Entity;

public class Member {
    private String memberId;
    private String name;
    private int points;
    private String tier;

    public Member(String memberId, String name, int points, String tier) {
        this.memberId = memberId;
        this.name = name;
        this.points = points;
        this.tier = tier;
    }

    // getter
    public String getMemberId() 
    { 
        return memberId; 
    }
    public String getName() 
    { 
        return name; 
    }
    public int getPoints() 
    { 
        return points; 
    }
    public String getTier()
    { 
        return tier; 
    }

    // setter
    public void setPoints(int points) { 
        this.points = points; 
        checkTierUpgrade(); 
    }

    // update tier when point changes 
    private void checkTierUpgrade() {
        
        if (this.points >= 5000) {
            this.tier = "Gold";
        } 
        else if (this.points >= 2000) {
            this.tier = "Silver";
        } 
        else {
            this.tier = "Bronze";
        }
    }

    // handle redemption point deduction 
    public boolean deductPoints(int cost) {
        
        if (this.points >= cost) {
            this.points -= cost;
            checkTierUpgrade();
            
            return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        return String.format("Member ID: %s | Name: %s | Tier: %s | Points: %d", memberId, name, tier, points);
    }
}