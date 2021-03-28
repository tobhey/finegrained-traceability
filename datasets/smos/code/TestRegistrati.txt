package smos.storage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import smos.bean.Assenza;
import smos.bean.Ritardo;
import smos.bean.Giustificare;
import smos.bean.Nota;
import smos.bean.RegistratiLinea;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.storage.ManagerRegistrati;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;


public class TestRegistrati {

	// database errato, impossibile inserire null nel campo id_justify di absence
	public static void main(String[] args) {
		
		ManagerRegistrati mr=  ManagerRegistrati.ottenereIstanza();
		
		Date datenow= new Date();
		
		Assenza absence = new Assenza();		
		absence.settareIdUtente(61);
		absence.settareDataAssenza(datenow);
		absence.settareIdGiustificare(0);
		absence.settareAnnoAccademico(2009);
		//absence.setIdAbsence(13);
		
		/*
		try {
			absence= mr.getAbsenceByIdAbsence(12);
			
		} catch (InvalidValueException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (EntitaNonTrovataEccezione e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		Giustificare justifynew= new Giustificare();
		justifynew.settareIdUtente(1);
		justifynew.settareDataGiustificare(datenow);
		justifynew.settareAnnoAccademico(2008);

		justifynew.settareIdGiustificare(6);
		
		
		Ritardo delay = new Ritardo();
		//delay.setIdDelay(3);
		delay.settareIdUtente(62);
		delay.settareDataRitardo(datenow);
		delay.settareTempoRitardo("10:00:00");
		delay.settareAnnoAccademico(2009);
		
		Nota notenew= new Nota();
		notenew.settareIdUtente(2);
		notenew.settareInsegnante("boh");
		notenew.settareAnnoAccademico(2009);
		notenew.settareDataNota(datenow);
		notenew.settareDescrizione("lo studente dorme in classe");
		notenew.settareIdNota(1);
		
		try{
			//mr.insertAbsence(absence);
			//if(mr.exists(absence)) System.out.println("esiste");;
			//mr.deleteAbsence(absence);
			//mr.updateAbsence(absence);
			
			mr.inserireRitardo(delay);
			//if(mr.exists(delay)) System.out.println("esiste");;
			//mr.updateDelay(delay);
			//mr.deleteDelay(delay);
			
			//mr.insertNote(notenew);
			//mr.deleteNote(notenew);
			
			//mr.insertJustify(justifynew, absence);
			//mr.deleteJustify(justifynew.ottenereIdGiustificare());
			//if(mr.hasJustify(absence))System.out.println("giustificata");
			
			//absence= mr.getAbsenceByIDUserAndDate(2, "2009-05-18");
			//absence= mr.getAbsenceByIdJustify(1);
			//System.out.println(absence.getDateAbsence());
			
			//Collection<Absence> ac= mr.getAbsenceByIDUserAndAcademicYear(2, 2009);
			//for(Absence x : ac) System.out.println(x.getDateAbsence());
			
			//justifynew=mr.getJustifyByAbsence(absence);
			//System.out.println(justifynew.getDateJustify());
			
			//Collection<Note> nc = mr.getNoteByIDUserAndAcademicYear(2, 2009);
			//for(Note x : nc) System.out.println(x.getDescription());
			
			//delay= mr.getDelayByIDUserAndDate(1, datenow);
			//System.out.println(delay.getDateDelay());
			//System.out.println(delay.getTimeDelay());
			
			
			///*
			Collection<RegistratiLinea> crl = mr.ottenereRegistratiPerClasseIDEData(64, datenow );
			for(RegistratiLinea x : crl){
				System.out.println(x.ottenereStudente().ottenereNome());
				if(mr.avereAssenza(x)){
					System.out.println("assente");
				}
				System.out.println(mr.avereAssenza(x));
				if(mr.avereRitardo(x)){
					System.out.println("ritardo");
				}
			}
			//*/
			
			GregorianCalendar gc = new GregorianCalendar();
	
			String date="";
			int year=gc.get(GregorianCalendar.YEAR);
			
			int month=gc.get(GregorianCalendar.MONTH)+1;
			String months="";
			if(month<10){
				months="0"+month;
			}else{
				months= months+month;
			}
			
			int day=gc.get(GregorianCalendar.DAY_OF_MONTH);
			String days="";
			if(day<10){
				days="0"+day;
			}else{
				days= days+day;
			}
			
			date= date + year+"-"+months+"-"+days;
			System.out.println(date);
			
		}catch (Exception e) {
			System.out.println("errore");
			System.out.println(e.getMessage());
		}
		
		System.out.println(datenow.toString());
		System.out.println(Utility.eNull(datenow));
		System.out.println("TEST COMPLETATO");
	}
	
}
