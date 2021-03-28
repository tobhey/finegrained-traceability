package edu.ncsu.csc.itrust.enums;

/**
 * All of our wonderful states.
 * 
 * @author Andy
 * 
 */
public enum State {
	AL("Alabama"), AK("Alaska"), AZ("Arizona"), AR("Arkansas"), CA("California"), CO("Colorado"), CT(
			"Connecticut"), DE("Delaware"), DC("District of Columbia"), FL("Florida"), GA("Georgia"), HI(
			"Hawaii"), ID("Idaho"), IL("Illinois"), IN("Indiana"), IA("Iowa"), KS("Kansas"), KY("Kentucky"), LA(
			"Louisiana"), ME("Maine"), MD("Maryland"), MA("Massachusetts"), MI("Michigan"), MN("Minnesota"), MS(
			"Mississippi"), MO("Missouri"), MT("Montana"), NE("Nebraska"), NV("Nevada"), NH("New Hampshire"), NJ(
			"New Jersey"), NM("New Mexico"), NY("New York"), NC("North Carolina"), ND("North Dakota"), OH(
			"Ohio"), OK("Oklahoma"), OR("Oregon"), PA("Pennsylvania"), RI("Rhode Island"), SC(
			"South Carolina"), SD("South Dakota"), TN("Tennessee"), TX("Texas"), UT("Utah"), VT("Vermont"), VA(
			"Virginia"), WA("Washington"), WV("West Virginia"), WI("Wisconsin"), WY("Wyoming");
	private String name;

	private State(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getAbbrev() {
		return toString();
	}

	// Just to show that this is intentional
	@Override
	public String toString() {
		return super.toString();
	}

	public static State parse(String state) {
		State[] values = State.values();
		for (State myState : values) {
			if (myState.getName().equals(state) || myState.getAbbrev().equals(state))
				return myState;
		}
		return State.NC;
	}
}
