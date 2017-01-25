package jw84_ym12_chatApp.view;

/**
 * Adapter the view uses to communicate to the model
 * @param <TDropList> 
 */
public interface IModelAdapter<TDropList> {
	/**
	 * Requests that model connect to the RMI Registry at the given remote host
	 * 
	 * @param remoteHost
	 *            The remote host to connect to.
	 * @return A status string regarding the connection result
	 */
	public String connectTo(String remoteHost);

	/**
	 * Quits the applications and gracefully shuts down the RMI-related resources.
	 */
	public void quit();

	/**
	 * Create a chat room
	 */
	public void createRoom();
	/**
	 * join a selected chatroom
	 * @param chatroom the selected chatroom
	 */
	public void joinRoom(TDropList chatroom);
	/**
	 * set the username you choose
	 * @param username the user name
	 */
	public void setUsername(String username);
		
}