

public class Player {
	
	private Integer playerId;
	private String playerName;
	private Integer playerScore;
	
	
	
	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Player(Integer playerId, String playerName, Integer playerScore) {
		super();
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerScore = playerScore;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Integer getPlayerScore() {
		return playerScore;
	}
	public void setPlayerScore(Integer playerScore) {
		this.playerScore = playerScore;
	}


	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", playerName=" + playerName + ", playerScore=" + playerScore + "]";
	}
	
	
	
	

}
