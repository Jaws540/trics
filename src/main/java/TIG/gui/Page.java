package TIG.gui;

import java.net.URL;

public class Page {
	
	private URL url;
	
	private Page previous = null;
	private Page next = null;
	
	public Page(URL url) {
		this.url = url;
	}
	
	public Page(URL url, Page prev) {
		this(url);
		this.previous = prev;
	}
	
	public Page(URL url, Page prev, Page next) {
		this(url);
		
		if(prev != null)
			this.previous = prev;
			
		if(next != null)
			this.next = next;
	}

	public String getUrl() {
		return url.toString();
	}
	
	public void setPrevious(Page prev) {
		this.previous = prev;
	}
	
	public void setNext(Page next) {
		this.next = next;
	}
	
	public Page back() {
		previous.setNext(this);
		return previous;
	}
	
	public Page forward(Page page) {
		this.next = page;
		page.previous = this;
		return next;
	}

}
