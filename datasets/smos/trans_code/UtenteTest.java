package smos.storage;

import java.sql.SQLException;
import java.util.Collection;

public class UserTest {

	/**
	 * @param args
	 * @throws ValueInvalidException
	 * @throws ConnectionException
	 * @throws EntityNotFoundException
	 * @throws SQLException
	 * @throws FieldRequiredException
	 */
	public static void main(String[] args) throws SQLException, EntityNotFoundException, ConnectionException,
			ValueInvalidException, FieldRequiredException {

		ItemListUsers temp = new ItemListUsers();
		temp.setId(5);
		Teaching teaching = new Teaching();
		teaching.setId(3);
		ManagerGrade mv = ManagerGrade.getInstance();
		Grade ghh = new Grade();
		ghh.setAcademicYear(2134);
		ghh.setId_user(88);
		ghh.setLaboratory(3);
		ghh.setOral(4);
		ghh.setTeaching(9);
		ghh.setRotation(2);
		ghh.setWriting(3);
		mv.insert(ghh);
	}
}
