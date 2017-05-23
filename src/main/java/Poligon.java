import com.turlygazhy.Bot;
import com.turlygazhy.reminder.timer_task.EveryNightTask;
import com.turlygazhy.tool.DateUtil;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 1/14/17.
 */
public class Poligon {

//    public static final String PATH = "/home/user/IdeaProjects/aslan-assistant2/tables/";
//    public static final String PATH_WITH_FOLDER = PATH + "StickyTableHeaders/";

//    private static String yourHTMLString = "<!DOCTYPE html>\n" +
//            "<html lang=\"en\" class=\"no-js\">\n" +
//            "<head>\n" +
//            "    <meta charset=\"UTF-8\"/>\n" +
//            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"" + PATH_WITH_FOLDER + "css/normalize.css\"/>\n" +
//            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"" + PATH_WITH_FOLDER + "css/demo.css\"/>\n" +
//            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"" + PATH_WITH_FOLDER + "css/component.css\"/>\n" +
//            "</head>\n" +
//            "<body>\n" +
//            "<div class=\"container\">\n" +
//            "    <div class=\"component\">\n" +
//            "        <table>\n" +
//            "            <thead>\n" +
//            "            <tr>\n" +
//            "                <th>Name</th>\n" +
//            "                <th>Email</th>\n" +
//            "                <th>Phone</th>\n" +
//            "                <th>Mobile</th>\n" +
//            "            </tr>\n" +
//            "            </thead>\n" +
//            "            <tbody>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">gary coleman</td>\n" +
//            "                <td class=\"user-email\">gary.coleman21@example.com</td>\n" +
//            "                <td class=\"user-phone\">(398)-332-5385</td>\n" +
//            "                <td class=\"user-mobile\">(888)-677-3719</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">rose parker</td>\n" +
//            "                <td class=\"user-email\">rose.parker16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(293)-873-2247</td>\n" +
//            "                <td class=\"user-mobile\">(216)-889-4933</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">chloe nelson</td>\n" +
//            "                <td class=\"user-email\">chloe.nelson18@example.com</td>\n" +
//            "                <td class=\"user-phone\">(957)-213-3499</td>\n" +
//            "                <td class=\"user-mobile\">(207)-516-4474</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">eric bell</td>\n" +
//            "                <td class=\"user-email\">eric.bell16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(897)-762-9782</td>\n" +
//            "                <td class=\"user-mobile\">(565)-627-3002</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">douglas hayes</td>\n" +
//            "                <td class=\"user-email\">douglas.hayes92@example.com</td>\n" +
//            "                <td class=\"user-phone\">(231)-391-6269</td>\n" +
//            "                <td class=\"user-mobile\">(790)-838-2130</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">cameron brown</td>\n" +
//            "                <td class=\"user-email\">cameron.brown32@example.com</td>\n" +
//            "                <td class=\"user-phone\">(204)-488-5204</td>\n" +
//            "                <td class=\"user-mobile\">(508)-463-6811</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">nevaeh diaz</td>\n" +
//            "                <td class=\"user-email\">nevaeh.diaz99@example.com</td>\n" +
//            "                <td class=\"user-phone\">(436)-578-2946</td>\n" +
//            "                <td class=\"user-mobile\">(906)-412-3302</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">kathy miller</td>\n" +
//            "                <td class=\"user-email\">kathy.miller62@example.com</td>\n" +
//            "                <td class=\"user-phone\">(724)-705-3555</td>\n" +
//            "                <td class=\"user-mobile\">(764)-841-2531</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">susan king</td>\n" +
//            "                <td class=\"user-email\">susan.king88@example.com</td>\n" +
//            "                <td class=\"user-phone\">(774)-205-7754</td>\n" +
//            "                <td class=\"user-mobile\">(639)-267-9728</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">jeffery ramirez</td>\n" +
//            "                <td class=\"user-email\">jeffery.ramirez83@example.com</td>\n" +
//            "                <td class=\"user-phone\">(723)-243-7706</td>\n" +
//            "                <td class=\"user-mobile\">(172)-597-3422</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">gary coleman</td>\n" +
//            "                <td class=\"user-email\">gary.coleman21@example.com</td>\n" +
//            "                <td class=\"user-phone\">(398)-332-5385</td>\n" +
//            "                <td class=\"user-mobile\">(888)-677-3719</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">rose parker</td>\n" +
//            "                <td class=\"user-email\">rose.parker16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(293)-873-2247</td>\n" +
//            "                <td class=\"user-mobile\">(216)-889-4933</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">chloe nelson</td>\n" +
//            "                <td class=\"user-email\">chloe.nelson18@example.com</td>\n" +
//            "                <td class=\"user-phone\">(957)-213-3499</td>\n" +
//            "                <td class=\"user-mobile\">(207)-516-4474</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">eric bell</td>\n" +
//            "                <td class=\"user-email\">eric.bell16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(897)-762-9782</td>\n" +
//            "                <td class=\"user-mobile\">(565)-627-3002</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">douglas hayes</td>\n" +
//            "                <td class=\"user-email\">douglas.hayes92@example.com</td>\n" +
//            "                <td class=\"user-phone\">(231)-391-6269</td>\n" +
//            "                <td class=\"user-mobile\">(790)-838-2130</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">cameron brown</td>\n" +
//            "                <td class=\"user-email\">cameron.brown32@example.com</td>\n" +
//            "                <td class=\"user-phone\">(204)-488-5204</td>\n" +
//            "                <td class=\"user-mobile\">(508)-463-6811</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">nevaeh diaz</td>\n" +
//            "                <td class=\"user-email\">nevaeh.diaz99@example.com</td>\n" +
//            "                <td class=\"user-phone\">(436)-578-2946</td>\n" +
//            "                <td class=\"user-mobile\">(906)-412-3302</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">kathy miller</td>\n" +
//            "                <td class=\"user-email\">kathy.miller62@example.com</td>\n" +
//            "                <td class=\"user-phone\">(724)-705-3555</td>\n" +
//            "                <td class=\"user-mobile\">(764)-841-2531</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">susan king</td>\n" +
//            "                <td class=\"user-email\">susan.king88@example.com</td>\n" +
//            "                <td class=\"user-phone\">(774)-205-7754</td>\n" +
//            "                <td class=\"user-mobile\">(639)-267-9728</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">jeffery ramirez</td>\n" +
//            "                <td class=\"user-email\">jeffery.ramirez83@example.com</td>\n" +
//            "                <td class=\"user-phone\">(723)-243-7706</td>\n" +
//            "                <td class=\"user-mobile\">(172)-597-3422</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">gary coleman</td>\n" +
//            "                <td class=\"user-email\">gary.coleman21@example.com</td>\n" +
//            "                <td class=\"user-phone\">(398)-332-5385</td>\n" +
//            "                <td class=\"user-mobile\">(888)-677-3719</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">rose parker</td>\n" +
//            "                <td class=\"user-email\">rose.parker16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(293)-873-2247</td>\n" +
//            "                <td class=\"user-mobile\">(216)-889-4933</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">chloe nelson</td>\n" +
//            "                <td class=\"user-email\">chloe.nelson18@example.com</td>\n" +
//            "                <td class=\"user-phone\">(957)-213-3499</td>\n" +
//            "                <td class=\"user-mobile\">(207)-516-4474</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">eric bell</td>\n" +
//            "                <td class=\"user-email\">eric.bell16@example.com</td>\n" +
//            "                <td class=\"user-phone\">(897)-762-9782</td>\n" +
//            "                <td class=\"user-mobile\">(565)-627-3002</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">douglas hayes</td>\n" +
//            "                <td class=\"user-email\">douglas.hayes92@example.com</td>\n" +
//            "                <td class=\"user-phone\">(231)-391-6269</td>\n" +
//            "                <td class=\"user-mobile\">(790)-838-2130</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">cameron brown</td>\n" +
//            "                <td class=\"user-email\">cameron.brown32@example.com</td>\n" +
//            "                <td class=\"user-phone\">(204)-488-5204</td>\n" +
//            "                <td class=\"user-mobile\">(508)-463-6811</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">nevaeh diaz</td>\n" +
//            "                <td class=\"user-email\">nevaeh.diaz99@example.com</td>\n" +
//            "                <td class=\"user-phone\">(436)-578-2946</td>\n" +
//            "                <td class=\"user-mobile\">(906)-412-3302</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">kathy miller</td>\n" +
//            "                <td class=\"user-email\">kathy.miller62@example.com</td>\n" +
//            "                <td class=\"user-phone\">(724)-705-3555</td>\n" +
//            "                <td class=\"user-mobile\">(764)-841-2531</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">susan king</td>\n" +
//            "                <td class=\"user-email\">susan.king88@example.com</td>\n" +
//            "                <td class=\"user-phone\">(774)-205-7754</td>\n" +
//            "                <td class=\"user-mobile\">(639)-267-9728</td>\n" +
//            "            </tr>\n" +
//            "            <tr>\n" +
//            "                <td class=\"user-name\">jeffery ramirez</td>\n" +
//            "                <td class=\"user-email\">jeffery.ramirez83@example.com</td>\n" +
//            "                <td class=\"user-phone\">(723)-243-7706</td>\n" +
//            "                <td class=\"user-mobile\">(172)-597-3422</td>\n" +
//            "            </tr>\n" +
//            "            </tbody>\n" +
//            "        </table>\n" +
//            "    </div>\n" +
//            "</div>\n" +
//            "</body>\n" +
//            "</html>";

    public static void main(String[] args) throws SQLException, InterruptedException {
        EveryNightTask everyNightTask = new EveryNightTask(null, null);
        everyNightTask.sendMonthChart(new Bot());


        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

        String date = "01.03.17";

        try {
            Date date1 = format.parse(date);
            System.out.println(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        DateUtil.getLastMonthWeeks();


//        org.jsoup.nodes.Document doms = Jsoup.parseBodyFragment(yourHTMLString);
//        doms.outputSettings().escapeMode(Entities.EscapeMode.xhtml); // avoid transformin the characters '<' and '>' as its html characters codes like &gt; and &lt;


//        imageGenerator.saveAsImage(PATH + new Date() + ".png");

//        imageGenerator.saveAsHtmlWithMap("hello-world.html", "hello-world.png");


//        String[] columnNames = {"First Name",
//                "Last Name",
//                "Sport",
//                "# of Years",
//                "Vegetarian"};
//
//        Object[][] data = {
//                {"Kathy", "Smith",
//                        "Snowboarding", new Integer(5), new Boolean(false)},
//                {"John", "Doe",
//                        "Rowing", new Integer(3), new Boolean(true)},
//                {"Sue", "Black",
//                        "Knitting", new Integer(2), new Boolean(false)},
//                {"Jane", "White",
//                        "Speed reading", new Integer(20), new Boolean(true)},
//                {"Joe", "Brown",
//                        "Pool", new Integer(10), new Boolean(false)}
//        };
//
//        JTable table = new JTable(data, columnNames);
//        table.setSize(500,500);
//
//        saveComponentAsJPEG(table, "/home/user/IdeaProjects/aslan-assistant2/tables/" + "table test2.jpg");


        //todo вывод чартов недельный и месячный общий в группу

        //todo админ может удалять собранные тезисы
        //todo добавить фото книги

//        Date date = new Date();
//        date.setDate(13);
//        String s = date.toString();
//        System.out.println(s);
//        System.out.println(s.contains("Mon"));
    }

//    public static void saveComponentAsJPEG(JTable table, String filename) {
//        java.awt.Image image = table.createImage(500, 500);
//
//        BufferedImage myImage =
//                new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
//
//
//        Graphics2D g2 = myImage.createGraphics();
//        table.paint(g2);
//
//        try {
//            java.io.OutputStream out = new FileOutputStream(filename);
//            com.sun.image.codec.jpeg.JPEGImageEncoder encoder =
//                    com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(myImage);
//            out.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public void saveComponentAsJPEG(Component myComponent, String filename) {
//        Dimension size = myComponent.getSize();
//        BufferedImage myImage =
//                new BufferedImage(size.width, size.height,
//                        BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = myImage.createGraphics();
//        myComponent.paint(g2);
//        try {
//            OutputStream out = new FileOutputStream(filename);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(myImage);
//            out.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

//        DaoFactory factory = DaoFactory.getFactory();
//        SavedResultsDao savedResultsDao = factory.getSavedResultsDao();
//        GoalDao goalDao = factory.getGoalDao();
//
//        int userId = 293188753;
//        List<UserResult> results = goalDao.getForUser(userId);
//        UserReadingResult reading = goalDao.getReadingResultForUser(userId);
//
//        savedResultsDao.insert(userId, results, reading);

//        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
//        String s = format.format(new Date());
//        System.out.println(s);

//        String[] s = {"01.03.17", "1.03.17", "1.3.17", "32.12.17", "11-02-17"};


//        Date date = new Date();
//        int month = date.getMonth();
//
//        while (month == date.getMonth()) {
//            date.setDate(date.getDate() + 1);
//        }
//        date.setDate(date.getDate() - 1);
//
//        System.out.println(date);
//
//
//        date.setDate(date.getDate() + 1);
//        while (date.getDate() != 1) {
//            date.setDate(date.getDate() + 1);
//        }
//        date.setHours(0);
//        date.setMinutes(0);
//        date.setSeconds(1);
//        System.out.println(date);

//        Chart.getChart("2", 10, 20, 30, 20, 60, 12);

//        boolean b = checkTime("08:00", "12:00", "08:00");
//        boolean b1 = checkTime("08:00", "12:00", "08:05");
//        boolean b2 = checkTime("08:00", "12:00", "12:00");
//        boolean b3 = checkTime("08:00", "12:00", "12:01");
//        boolean b4 = checkTime("08:00", "12:00", "09:00");
//        boolean b5 = checkTime("08:00", "12:00", "07:00");
//        System.out.println(b);

//        Timer timer = new Timer("night waiter", true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Yeeeeeear");
//            }
//        }, 10000);
//        for (; ; ) {
//            Thread.sleep(1000);
//            System.out.println("boring");
//        }

//        DaoFactory factory = DaoFactory.getFactory();
//        ReservationDao reservationDao = factory.getReservationDao();
//        reservationDao.selectAvailableTime(1);
//        MemberDao memberDao = factory.getMemberDao();
//        boolean userRegistered = memberDao.isUserRegistered(213188753);
//        System.out.println(userRegistered);
//    }

//    private static boolean checkTime(String startTime, String endTime, String nowTime) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); //HH = 24h format
//            dateFormat.setLenient(false); //this will not enable 25:67 for example
//
//            Date now = dateFormat.parse(nowTime);
//            int nowHours = now.getHours();
//            int nowMinutes = now.getMinutes();
//
//            Date start = dateFormat.parse(startTime);
//            int startHours = start.getHours();
//            int startMinutes = start.getMinutes();
//
//            Date end = dateFormat.parse(endTime);
//            int endHours = end.getHours();
//            int endMinutes = end.getMinutes();
//
//            if (startHours < nowHours) {
//                if (endHours > nowHours) {
//                    return true;
//                } else {
//                    if (endHours == nowHours) {
//                        if (endMinutes >= nowMinutes) {
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    } else {
//                        return false;
//                    }
//                }
//            } else {
//                if (startHours == nowHours) {
//                    if (startMinutes <= nowMinutes) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                } else {
//                    return false;
//                }
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//    }


//    private static void clearTable() throws SQLException {
//        Connection connection = ConnectionPool.getConnection();
//        String tableName = "";//set here table name
//        PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tableName);
//        ps.execute();
//        ResultSet rs = ps.getResultSet();
//        while (rs.next()) {
//            int id = rs.getInt(1);
//            PreparedStatement psDel = connection.prepareStatement("DELETE FROM " + tableName + " WHERE ID=?");
//            psDel.setInt(1, id);
//            psDel.execute();
//        }
//    }

    /*PreparedStatement ps = connection.prepareStatement("INSERT INTO RESERVATION  VALUES(?, ?, null, null)");
        int id = 27;
        int min0 = 0;
        int hour = 10;
        while (true) {
            ps.setInt(1, id);
            ps.setString(2, day + "-" + hour + ":" + min0 + "0");
            ps.execute();
            if (min0 == 0) {
                min0 = 3;
            } else {
                min0 = 0;
                hour++;
            }
            id++;
            if (hour == 23) {
                return null;
            }
        }*/
}
