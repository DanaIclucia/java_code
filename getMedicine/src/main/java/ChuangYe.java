import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.*;

public class ChuangYe {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        //启用开发者模式规避selenium的反爬
        ChromeOptions option = new ChromeOptions();
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        option.addArguments("--disable-blink-features=AutomationControlled");
        //打开浏览器
        ChromeDriver browser = new ChromeDriver(option);
        browser.get("https://passport.zhihuishu.com/login?service=https://onlineservice-api.zhihuishu.com/gateway/f/v1/login/gologin");
        Actions action = new Actions(browser);

        while (true) {
            try {
                Thread.sleep(2000);
                browser.findElementByXPath("//*[@id=\"qStudentID\"]").click();
                Thread.sleep(2000);
                browser.findElementByXPath("//*[@id=\"layui-layer1\"]/div/div/div/a").click();
                Thread.sleep(2000);
                browser.findElementByXPath("//*[@id=\"quickSearch\"]").sendKeys("山东大学");
                Thread.sleep(2000);
                browser.findElementByXPath("//*[@id=\"schoolListCode\"]/li[2]").click();
                Thread.sleep(1000);
                browser.findElementByXPath("//*[@id=\"clCode\"]").sendKeys("");
                browser.findElementByXPath("//*[@id=\"clPassword\"]").sendKeys("");
//                browser.findElementByXPath("//*[@id=\"f_sign_up\"]/div[1]/span").click();
                break;
            } catch (Exception e) {
                System.out.println("第一个while循环中");
            }
        }

        while (true){
            System.out.println("等待视频");
            System.out.println("输入0退出");
            System.out.println("输入1创业课");
            System.out.println("输入2马毛课");
            Scanner scanner = new Scanner(System.in);
            String a = scanner.nextLine();
            if(a.equals("0"))
                break;
            else if(a.equals("1"))
                ChuangYeKe(browser, action);
            else if (a.equals("2"))
                MaoGai(browser, action);
            else {
                System.out.println("不合法输入");
                System.out.println();
            }
        }
    }

    static void ChuangYeKe(ChromeDriver browser, Actions action) throws InterruptedException {
        Date now_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now_date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int i = 0;
        //判断是否出现异常的暂停播放现象
        String first_play_time;
        while (true){
            if(Quit_movie(hour,minute)){
                //到时间如果不在做题界面就自动暂停
                try {
                    browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[8]").click();
                }
                catch (Exception e){
                    System.out.println("异常暂停自动播放方法没有执行成功");
                }
                break;
            }

            if (i == 0){
                i = 1;
                Next_movie(browser, action);
                first_play_time = now_play_time(browser, action);
                Thread.sleep(1000);
                if(first_play_time.equals(now_play_time(browser, action))){
                    try {
                        browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[8]").click();
                    }
                    catch (Exception e){
                        System.out.println("异常暂停自动播放方法没有执行成功，可能是目前处于做题界面");
                    }
                }
            }
            else {
                i = 0;
                Do_question(browser);
            }
        }
    }
    static void Next_movie(ChromeDriver browser, Actions action) {
        try {
            //两次移动鼠标位置呼出下一集按键
            Thread.sleep(3000);
            action.moveToElement(browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[8]")).perform();
            action.moveToElement(browser.findElementByXPath("//*[@id=\"app\"]/div/div[2]/div[2]/ul/li[1]")).perform();
            String now_time = browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[10]/div[4]/span[1]").getText();
            String all_time = browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[10]/div[4]/span[2]").getText();
            if (Objects.equals(now_time, all_time))
                browser.findElementByXPath("//*[@id=\"nextBtn\"]").click();
            else
                System.out.println("这个视频没有播放结束进度条"+now_time+"/"+all_time);
        } catch (Exception e) {
            System.out.println("Next_movie方法没有执行完毕，该方法执行出现错误");
        }
    }
    static void Do_question(ChromeDriver browser) {
        try {
            browser.findElementByXPath("//*[@id=\"playTopic-dialog\"]/div/div[2]/div/div[1]/div/div/div[2]/ul/li[1]").click();
            Thread.sleep(2000);
            browser.findElementByXPath("//*[@id=\"playTopic-dialog\"]/div/div[3]/span/div").click();
            Thread.sleep(2000);
            //通过点击播放屏幕来继续播放
            browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[8]").click();
        } catch (Exception e) {
            System.out.println("Do_question方法没有执行完毕，目前不需要该方法执行");
        }
    }
    static boolean Quit_movie(int begin_hour, int begin_minute){
        Date now_date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now_date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int all_minute = (hour-begin_hour)*60+(minute-begin_minute);
        System.out.println("现在已经看了"+all_minute+"分钟的视频");
        return all_minute >= 28;
    }
    static String now_play_time(ChromeDriver browser, Actions action){
        try {
            action.moveToElement(browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[8]")).perform();
            action.moveToElement(browser.findElementByXPath("//*[@id=\"app\"]/div/div[2]/div[2]/ul/li[1]")).perform();
            return browser.findElementByXPath("//*[@id=\"vjs_container\"]/div[10]/div[4]/span[1]").getText();
        } catch (Exception e) {
            System.out.println("now_play_time方法没有执行完毕，可能是目前正处于做题界面");
        }
        return "不可能return的结果";
    }

    static void MaoGai(ChromeDriver browser, Actions action) throws InterruptedException {
        //获取所有窗口句柄
        Set<String>Windows = browser.getWindowHandles();
        //把获取到的窗口句柄放到list中
        List<String>allWindows = new ArrayList<String>(Windows);
        browser.switchTo().window(allWindows.get(1));

        Thread.sleep(2000);
        int start_movie_Xpath_int =  find_next_movie_to_start_file_int(browser,action);
        System.out.println("find_next_movie_to_start_file_int方法返回的结果是"+start_movie_Xpath_int);
        browser.findElementByXPath("//*[@id=\"file_"+start_movie_Xpath_int+"\"]/span").click();

        int i = 0;
        while(i != 200){
            i++;
            start_movie_Xpath_int = play_next_movie_mao(browser, action, start_movie_Xpath_int);
        }
        //方法结束窗口切换回去
        browser.switchTo().window(allWindows.get(0));
    }

    static int find_next_movie_to_start_file_int(ChromeDriver browser, Actions action){
        int a = 0;
        String start_movie = "//*[@id=\"file_" + (a+12451745) + "\"]/div";
        while(true){
            //如果获取的html语句是<i class="icon-finish"></i>
            //表明这个视频完播，这不是要播放的视频
            try {
                if(Objects.equals(browser.findElementByXPath(start_movie).getAttribute("innerHTML"), "<i class=\"icon-finish\"></i>"))
                {
                    a++;
                    start_movie = "//*[@id=\"file_" + (a+12451745) + "\"]/div";
                }
                else
                    break;
            }
            catch (Exception e){
                System.out.println("表明没有找到那个元素,需要调整滑动条");
                int x = browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div").getLocation().getX();
                int y = browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div").getLocation().getY();
                //每次调整20px
                action.dragAndDropBy(browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div"),x,y+20).perform();
            }
        }
        return a+12451745;
    }

    static int play_next_movie_mao(ChromeDriver browser, Actions action, int start_movie_Xpath_int){
        while(true){
            try {
                Thread.sleep(5000);
                action.moveToElement(browser.findElementByXPath("//*[@id=\"vjs_mediaPlayer\"]/div[8]")).perform();
                action.moveToElement(browser.findElementByXPath("//*[@id=\"qaPopDetail\"]")).perform();
                String first_time = browser.findElementByXPath("//*[@id=\"vjs_mediaPlayer\"]/div[10]/div[4]/span[1]").getText();
                String second_time = browser.findElementByXPath("//*[@id=\"vjs_mediaPlayer\"]/div[10]/div[4]/span[2]").getText();
                System.out.println(first_time+"/"+second_time);

                if(Objects.equals(first_time, "00:00:00")){
                    browser.findElementByXPath("//*[@id=\"vjs_mediaPlayer\"]/div[8]").click();
                    System.out.println("视频没有播放，点击播放");
                }
                if(first_time.equals(second_time))
                {
                    while (true){
                        try {
                            browser.findElementByXPath("//*[@id=\"file_"+start_movie_Xpath_int+"\"]/span").click();
                            System.out.println("点击下一个视频，并返回下一次要点击的视频的序列号");
                            return start_movie_Xpath_int+1;
                        }
                        catch (Exception e){
                            System.out.println("没有找到这个课，需要移动滑动条");
                            int x = browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div").getLocation().getX();
                            int y = browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div").getLocation().getY();
                            //每次调整20px
                            action.dragAndDropBy(browser.findElementByXPath("/html/body/div[1]/div[2]/div[2]/div[2]/div"),x,y+20).perform();
                        }
                    }
                }
            }
            catch (Exception e){
                System.out.println("Next_movie_mao前半部分寻找时间出现执行错误");
            }
        }
    }
}
