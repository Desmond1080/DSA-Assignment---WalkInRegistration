package Entity;

import Entity.Member;

public class RedemptionRequest {
    private Member member;
    private String rewardItem;
    private int pointsCost;

    public RedemptionRequest(Member member, String rewardItem, int pointsCost) {
        this.member = member;
        this.rewardItem = rewardItem;
        this.pointsCost = pointsCost;
    }

    public Member getMember() { return member; }
    public String getRewardItem() { return rewardItem; }
    public int getPointsCost() { return pointsCost; }

    @Override
    public String toString() {
        return String.format("[%s] requested '%s' (Cost: %d points)", member.getName(), rewardItem, pointsCost);
    }
}