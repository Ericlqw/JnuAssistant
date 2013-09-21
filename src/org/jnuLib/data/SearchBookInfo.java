package org.jnuLib.data;
/**
 * @author Eric
 *
 * 2013-8-12
 * 检索书本时，存储从网页中获得的数据，即书本的简略信息和网页中的其他信息
 */
public class SearchBookInfo {
	
private String booktitle=null;//书名
private String bookHref=null;//链接

private String chubanshe=null;//出版社

public String getBooktitle() {
	return booktitle;
}
public void setBooktitle(String booktitle) {
	this.booktitle = booktitle;
}
public String getBookHref() {
	return bookHref;
}
public void setBookHref(String bookHref) {
	this.bookHref = bookHref;
}

public String getChubanshe() {
	return chubanshe;
}
public void setChubanshe(String chubanshe) {
	this.chubanshe = chubanshe;
}

}
