
public class Talker {
  public static String[] splitMessage(String s) {
    System.out.println("sz: " + s.length());
    int cnt = 0;
    int i = 1;
    int step = 10;

    int n = s.length() / 10;
    int rem = s.length() % 10;
    if (rem > 0) {
      n += 1;
    }

    String[] ss = new String[n + 1];

    ss[0] = Integer.toString(n);

    while (cnt < s.length()) {

      if ((cnt + step) >= s.length()) {
        step = s.length() - cnt;
      }

      ss[i] = s.substring(cnt, cnt + step);

      cnt += step;
      i += 1;
    }

    return ss;

  }

}