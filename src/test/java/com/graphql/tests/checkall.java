package com.graphql.tests;

class mobile{
	static String staticname;
	String nonstaticname;
	String thisvariable;
	
	void n() {
	checkthismethod();
	}

	void checkthismethod() {
		System.out.println("check this method");
	}
}


public class checkall {
	
	public static void main(String args[]) {
		String thisvariable = "Localvariable";
		
		System.out.println(thisvariable);
		
		//1 .WHY STRING IMMUTABLE in JAVA
		String a = "Ram";
		a.concat("Kumar");
		// The reference will be pointed to String constant pool*/
		System.out.println(a);
		
		String b = "Ram";
		b = b.concat("Kumar");
		// The reference will be pointed to Heap memory and String constant pool*/
		System.out.println(b);
		
		//2. STATIC - as string is declared as static. We can refer the class name and use the variable directly.
		mobile.staticname = "Iphone";
		
		//NON STATIC - as string is declared as non static. We need to create object for that class name and use the variable.
		mobile m1 = new mobile();
		m1.nonstaticname = "Android";
		
		
		m1.n();
		
	}

	

	

}
