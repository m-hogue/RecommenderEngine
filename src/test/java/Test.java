import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	public static void main(String[] args) {

		String val = "test/                                                                                               0000755 0000000 0000000 00000000000 12326407260 010535  5                                                                                                    ustar   root                            root                                                                                                                                                                                                                   test/mv_0017730.txt                                                                                 0000644 0000000 0000000 00000305364 12326263023 012631  0                                                                                                    ustar   root                            root                                                                                                                                                                                                                   17730:";

		Matcher m = Pattern.compile("\\d+:").matcher(val);
		System.out.println(m.find());
		System.out.println(m.group(0));
		
		if(val.matches(".*\\d+:$")){
			String[] parts = val.split("\\s+");
			String result = parts[parts.length-1];
			System.out.println("result: " +result);
		} else 
			System.out.println("not");
	}

}
