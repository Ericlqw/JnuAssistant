package org.jnuLib.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jnuLib.data.GlobleData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class JsoupUtil {


	/**
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getSearchBookInfo(String url) throws Exception{
		String totlePage=null;
		String nextPage=null;
		String prePage=null;

		List<Map<String, Object>> InfoList=new ArrayList<Map<String, Object>>();
		try { 
			//获取目标链接的Document 
			Document doc = Jsoup.connect(url).timeout(30*1000).get(); 						

			//获取下一页
			Elements next_pre_page=doc.select("div.pagination");							

			//下一页的链接
			nextPage=next_pre_page.select("a.next").attr("abs:href");
			GlobleData.NEXTPAGE=nextPage;
			//	System.out.println("下一页："+nextPage);
			//上一页的链接
			prePage=next_pre_page.select("a.prev").attr("abs:href");
			GlobleData.PREPAGE=prePage;

			//	System.out.println("shang一页："+prePage);

			//获取总记录数
			totlePage=doc.select("div.tips").text();
			String s1[]=totlePage.split("有");
			String s2[]=s1[1].split("条");
			totlePage=s2[0];

			totlePage=totlePage.substring(1,totlePage.length()-1 );
			//	System.out.println("页："+totlePage);

			int allPage=Integer.parseInt(totlePage)%12;

			int p=(allPage==0)?(Integer.parseInt(totlePage)/12):
				(Integer.parseInt(totlePage)/12)+1;



			GlobleData.TOTLEPAGE=Integer.toString(p);
			GlobleData.TOTLERECORD=totlePage;

			//获取图书信息
			Elements bookInfo=doc.select("ul.searchList").select("a");
			//获取书名
			Elements bookname=bookInfo.select("span.name");

			//获取书的简介
			Elements bookdetail=bookInfo.select("span.info");

			for(int i=0;i<bookInfo.size();i++){
				HashMap<String, Object>	bookMap = new HashMap<String, Object>();

				//书名和链接
				bookMap.put("bookTitle", bookname.get(i).text());
				bookMap.put("bookHref", bookInfo.get(i).attr("abs:href"));
				System.out.println("\n"+bookname.get(i).text());
				System.out.println("\n"+bookInfo.get(i).attr("abs:href"));
				//简介
				bookMap.put("bookDetail",bookdetail.get(i).text());
				System.out.println("\n"+bookdetail.get(i).text());
				InfoList.add(bookMap);				
			}	
			return InfoList;

		} catch (IOException e) {          
			e.printStackTrace(); 
		} 
		return null;
	}


	public static List<Map<String, Object>> getBookDetailInfo(String url) throws Exception{

		List<Map<String, Object>> InfoList=new ArrayList<Map<String, Object>> ();
		Document doc = Jsoup.connect(url).timeout(30*1000).get(); 		

		//获取书本详细信息
		Elements detailInfo=doc.select("div.detailList");
		String s=detailInfo.toString();

		//	System.out.println(s+"\n");
		s=s.replaceAll("&nbsp", " ");
		String []detail=s.split("<br /> ");
		detail[0]=detail[0].substring(24,detail[0].length());

		/*	for(String a:detail){
				i++;
				//去掉前面的div标签					

				System.out.println(i+":"+a);

			}*/
		HashMap<String, Object>	bookInfoMap = new HashMap<String, Object>();
		bookInfoMap.put("BookTitle", detail[0]);//名字
		bookInfoMap.put("Author", detail[1]);//作者，编者
		bookInfoMap.put("Publisher", detail[2]);//出版社
		bookInfoMap.put("FuZhu", detail[3]);//附注
		bookInfoMap.put("ZaiTi", detail[4]);// 载体形式
		bookInfoMap.put("BiaoZhunHao", detail[5]);//标准号
		bookInfoMap.put("ZhuTi", detail[6]);//主题

		InfoList.add(bookInfoMap);


		//获取馆藏信息
		Elements Info=doc.select("div.sheet").select("td");		
		for(Element e:Info){
			HashMap<String, Object>	guancangMap = new HashMap<String, Object>();
			guancangMap.put("GuanCang", e.text());
			InfoList.add(guancangMap);
		}		 

		return InfoList;
	}

	/**
	 * 获取教务处最新通知
	 * @param url  http://jwcweb.jnu.edu.cn/index-new.asp
	 */
    
	public static List<Map<String, Object>> getJWCNews(String url) {
		 
		List<Map<String, Object>> InfoList=new ArrayList<Map<String, Object>> ();

		
		 try { 
             //获取目标链接的Document 
             Document doc = Jsoup.connect(url).get(); 
             //获取所有input标签         
             Elements els = doc.getElementsByTag("A"); 
                     
         //    System.out.println("\n\n\n"+els+"\n"); 
      
         
       	  System.out.println("------------------------------解析后的内容--------------------------");
       	  System.out.println("\t");
             for (Element link : els) {
            	  String linkHref = link.attr("abs:href");   //这样添加abs可以获取到完整的url，不过还是要解析
            	  String linkText = link.text();	  
            	  /**由于直接获取回来的url不能直接使用，需要把中文转码才能访问该链接
            	   * 所以需要把url拆开，把中文名字编码后再重构
            	   */
            	  linkHref.replace("教务处", "0");
            	  linkHref.replace("通知", "0");
    	
            //	  System.out.println(link.nodeName()+":\t"+linkHref);
            //	  System.out.println(linkText); 
                  
             }
             //获取时间
             Elements dates=doc.select("td.unnamed1");
             
             
         	
 			for(int i=0;i<dates.size();i++){
 				HashMap<String, Object>	bookMap = new HashMap<String, Object>();
 						
 				  String linkHref = els.get(i).attr("abs:href");   //这样添加abs可以获取到完整的url，不过还是要解析
            	  String linkText = els.get(i).text();	  
            	  /**由于直接获取回来的url不能直接使用，需要把中文转码才能访问该链接
            	   * 所以需要把url拆开，把中文名字编码后再重构
            	   */
            	  linkHref.replace("教务处", "0");
            	  linkHref.replace("通知", "0");
            	  
 				//书名和链接
 				bookMap.put("newsTitle", els.get(i).text());
 				System.out.println("\n"+els.get(i).text());
 				bookMap.put("newsHref",els.get(i).attr("abs:href"));
 				
 			//	System.out.println("\n"+els.get(i).attr("abs:href"));

 				//简介
 				bookMap.put("newsDate",dates.get(i).text());
 			//	System.out.println("\n"+dates.get(i).text());

 				InfoList.add(bookMap);				
 			}	
 			return InfoList;
             
        } catch (IOException e) {          
            e.printStackTrace(); 
        } 
		return null;
		
	}
	
	public static String getJWCNewsDetail(String url) throws Exception{
		 String s = null;
		 try { 
            //获取目标链接的Document 
            Document doc = Jsoup.connect(url).get(); 
            //获取所有input标签         
            Elements els = doc.select("font.news"); 
            
                    
           // System.out.println("\n\n\n"+els.text()+"\n"); 
            s=els.toString();
     
       } catch (IOException e) {          
           e.printStackTrace(); 
       } 
		return s;
		
	}

	 private static String sendhttpClientPost(String stu_no,String stu_name) {
   	  String url="http://mynet.jnu.edu.cn/zhwl/login.asp";  
   	  String html = null;
         //POST的URL  
         HttpPost httppost=new HttpPost(url);  
         //建立HttpPost对象  
         List<NameValuePair> params=new ArrayList<NameValuePair>();  
         //建立一个NameValuePair数组，用于存储欲传送的参数  
         params.add(new BasicNameValuePair("id_num",stu_no));               
         params.add(new BasicNameValuePair("u_name",stu_name));
       //  params.setContentCharset("UTF-8");  
         //添加参数  
         try {
			httppost.setEntity(new UrlEncodedFormEntity(params,"GBK"));		
         //设置编码  
             HttpResponse response = null;
	          response = new DefaultHttpClient().execute(httppost);
		if(response.getStatusLine().getStatusCode()==200){//如果状态码为200,就是正常返回  
		         		
			html = EntityUtils.toString(response.getEntity());
			//处理乱码，对取得的result字符串作下转换
			html=new String(html.getBytes("ISO-8859-1"),"GBK");  
			 //得到返回的字符串  
         //  System.out.println(html);  
           //打印输出  
     //如果是下载文件,可以用response.getEntity().getContent()返回InputStream 
		}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return html;
	              	
         //发送Post,并返回一个HttpResponse对象  
                 //Header header = response.getFirstHeader("Content-Length");  
         //String Length=header.getValue();  
                 // 上面两行可以得到指定的Header       
   }
	 
	 public static Elements getNetInfo(String stu_no,String stu_name) {
		 String html=sendhttpClientPost(stu_no,stu_name);
	 //获取目标链接的Document 
		Document doc = Jsoup.parse(html);
		//获取所有input标签         
		Elements els = doc.getElementsByTag("td"); 
		if(els.text()=="")
		System.out.println("result:"+els);
		
		/*int i=0;
		for(Element e:els){
			System.out.println(i+e.text());
			i++;
		}*/
		//

		return els; 
			 
		 }
	 //获取校内招聘会
	 public static Elements getJnuZhaopinhui(String url)throws Exception{
 	    
          Document doc = Jsoup.connect(url).get(); 
           Elements els=doc.select("div.page3_content_t");
           System.out.println(els.text());
           
           Elements c=doc.select("div.page3_content_m");
           System.out.println("\n"+c);
		return c;

}
	 //获取应届生招聘会
	 public static Elements getyingjiesZhaopinhui(String url)throws Exception{
		    
         Document doc = Jsoup.connect(url).get(); 
          Elements els=doc.select("table.list");
          els=els.select("tr").select("td");
          
          System.out.println(els);
          
      
	return els;
	
	
}
	 //获取珠海招聘会
	 public static Elements getzhuhaiZhaopinhui(String url)throws Exception{
		    
	        Document doc = Jsoup.connect(url).get(); 
	         Elements els=doc.select("table.list");
	         System.out.println(els);
	         els=els.select("tr").select("td");
	      //   Elements c=doc.select("div.page3_content_m");
	       //  System.out.println("\n"+c);
		return els;
		
		
	}
	 }
