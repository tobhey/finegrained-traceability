package smos.storage;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestRegistration {

	// wrong database, unable to insert null in the id_excuse field of absence
	public static void main(String[] args) {

		ManagerRegister mr = ManagerRegister.getInstance();

		Date datenow = new Date();

		Absence absence = new Absence();
		absence.setIdUser(61);
		absence.setDateOfAbsence(datenow);
		absence.setIdExcuse(0);
		absence.setAcademicYear(2009);
		// absence.setIdAbsence(13);

		/*
		 * try { absence= mr.getAbsenceByIdAbsence(12);
		 * 
		 * } catch (InvalidValueException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } catch (EntityNotFoundException e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); } catch (ConnectionException
		 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); } catch
		 * (SQLException e1) { // TODO Auto-generated catch block e1.printStackTrace();
		 * }
		 */

		Excuse excusenew = new Excuse();
		excusenew.setIdUser(1);
		excusenew.setDataExcuse(datenow);
		excusenew.setAcademicYear(2008);

		excusenew.setIdExcuse(6);

		Delay delay = new Delay();
		// delay.setIdDelay(3);
		delay.setIdUser(62);
		delay.setDateDelay(datenow);
		delay.setTimeDelay("10:00:00");
		delay.setAcademicYear(2009);

		Note notenew = new Note();
		notenew.setIdUser(2);
		notenew.setTeacher("boh");
		notenew.setAcademicYear(2009);
		notenew.setDataNote(datenow);
		notenew.setDescription("lo student dorme in classe");
		notenew.setIdNote(1);

		try {
			// mr.insertAbsence(absence);
			// if(mr.exists(absence)) System.out.println("exists");;
			// mr.deleteAbsence(absence);
			// mr.updateAbsence(absence);

			mr.insertDelay(delay);
			// if(mr.exists(delay)) System.out.println("exists");;
			// mr.updateDelay(delay);
			// mr.deleteDelay(delay);

			// mr.insertNote(notenew);
			// mr.deleteNote(notenew);

			// mr.insertJustify(excusenew, absence);
			// mr.deleteJustify(excusenew.getIdExcuse());
			// if(mr.hasJustify(absence))System.out.println("giustificata");

			// absence= mr.getAbsenceByIDUserAndDate(2, "2009-05-18");
			// absence= mr.getAbsenceByIdExcuse(1);
			// System.out.println(absence.getDateAbsence());

			// Collection<Absence> ac= mr.getAbsenceByIDUserAndAcademicYear(2, 2009);
			// for(Absence x : ac) System.out.println(x.getDateAbsence());

			// excusenew=mr.getJustifyByAbsence(absence);
			// System.out.println(excusenew.getDateJustify());

			// Collection<Note> nc = mr.getNoteByIDUserAndAcademicYear(2, 2009);
			// for(Note x : nc) System.out.println(x.getDescription());

			// delay= mr.getDelayByIDUserAndDate(1, datenow);
			// System.out.println(delay.getDateDelay());
			// System.out.println(delay.getTimeDelay());

			/// *
			Collection<RegisterLine> crl = mr.getRegistrationPerClassIDEData(64, datenow);
			for (RegisterLine x : crl) {
				System.out.println(x.getStudent().getName());
				if (mr.haveAbsence(x)) {
					System.out.println("assente");
				}
				System.out.println(mr.haveAbsence(x));
				if (mr.haveDelay(x)) {
					System.out.println("delay");
				}
			}
			// */

			GregorianCalendar gc = new GregorianCalendar();

			String date = "";
			int year = gc.get(Calendar.YEAR);

			int month = gc.get(Calendar.MONTH) + 1;
			String months = "";
			if (month < 10) {
				months = "0" + month;
			} else {
				months = months + month;
			}

			int day = gc.get(Calendar.DAY_OF_MONTH);
			String days = "";
			if (day < 10) {
				days = "0" + day;
			} else {
				days = days + day;
			}

			date = date + year + "-" + months + "-" + days;
			System.out.println(date);

		} catch (Exception e) {
			System.out.println("errore");
			System.out.println(e.getMessage());
		}

		System.out.println(datenow.toString());
		System.out.println(Utility.eNull(datenow));
		System.out.println("TEST COMPLETATO");
	}

}
