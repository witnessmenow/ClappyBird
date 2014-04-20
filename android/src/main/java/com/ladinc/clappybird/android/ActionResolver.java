package com.ladinc.clappybird.android;

public interface ActionResolver {

	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score);
	public void unlockAchievementGPGS(int score);
	public void getLeaderboardGPGS();
	public void getAchievementsGPGS();  
}
