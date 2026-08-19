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

    // Getters and Setters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getPoints() { return points; }
    public String getTier() { return tier; }

    public void setPoints(int points) { this.points = points; checkTierUpgrade(); }

    // Logic to automatically upgrade tier based on points
    private void checkTierUpgrade() {
        if (this.points >= 5000) this.tier = "Platinum";
        else if (this.points >= 2000) this.tier = "Diamond";
        else if (this.points >= 1000) this.tier = "Elite";
        else this.tier = "Standard";
    }

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