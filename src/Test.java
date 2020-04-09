import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        String s = "HNGRYUA";
        ArrayList<String> found = getCombinations(s);
        System.out.println(found.toString());
    }
    public static ArrayList<String> getCombinations(String s)
    {
        ArrayList<String> allCombinations = new ArrayList<String>();
        permute("", s, allCombinations);
        combo("", s, allCombinations);
        return allCombinations;
    }

    static void combo(String prefix, String s, ArrayList<String> allCombinations)
    {
        int N = s.length();

        allCombinations.add(prefix);

        for (int i = 0 ; i < N ; i++)
            combo(prefix + s.charAt(i), s.substring(i+1), allCombinations);
    }

    static void permute(String prefix, String s, ArrayList<String> allCombinations)
    {
        int N = s.length();

        if (N == 0)
            allCombinations.add(prefix);

        for (int i = 0 ; i < N ; i++)
            permute(prefix + s.charAt(i), s.substring(0, i) + s.substring(i+1, N), allCombinations);
    }
}
