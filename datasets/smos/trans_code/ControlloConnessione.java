package smos.storage.connectionManagement;

import java.sql.*;

/**
 * Thread implementation capable of maintaining database connection Mysql if
 * there are no more active connections (problem solving Mysql autoreconnect).
 * This class also takes care of recalling the method of releasing active
 * connections that have not performed operations over a certain period of time.
 */
public class ControlConnection extends Thread {
	private static SourceDataConnectionPool manager = null;

	private static int waitTimeout;

	/**
	 * Create a new instance of the Thread.
	 *
	 * @author Di Giorgio Domenico, Melancholy Cris
	 * @param pManager the connection pool currently running.
	 */
	public ControlConnection(SourceDataConnectionPool pManager) {
		ControlConnection.manager = pManager;
	}

	/**
	 * Create a new instance of the Thread.
	 *
	 * @param pManager the connection pool currently running.
	 * @param pTime    The time to reestablish the connection with mysql first that
	 *                 expires.This value must necessarily be less than value of the
	 *                 global Mysql wait_timeout variable.
	 */
	public ControlConnection(SourceDataConnectionPool pManager, int pTime) {
		ControlConnection.waitTimeout = pTime;
		ControlConnection.manager = pManager;
	}

	/**
	 * The thread does nothing but sleep when there are active users and keep the
	 * connection with MySQL open otherwise.
	 * 
	 */
	@Override
	public void run() {
		try {
			while (true) {
				if (manager.activeSize() > 0) {
					this.setPriority(Thread.MAX_PRIORITY);
					manager.clearActive();
					this.setPriority(Thread.NORM_PRIORITY);
				}
				if (manager.activeSize() == 0) {
					while (true) {
						try {
							manager.closeAllInPoolConnections();
							Connection con = null;
							con = manager.getConnection();
							Statement st = con.createStatement();
							st.executeQuery("show tables");
							manager.release(con);
							break;
						} catch (Exception e) {
							System.out.println("Exception thrown" + "in the Thread ControlConnection:" + e);
						}
					}
					Thread.sleep(waitTimeout);
				} else {
					Thread.sleep(waitTimeout);
				}
			}
		} catch (InterruptedException ex) {
			System.out.println("Thread ControlConnection stopped:" + ex);
		}
	}
}
