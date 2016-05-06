package com.hackensack.umc.datastructure;

public class RssItem {
	 
    private String title;
    private String link;
    private String description;

    public RssItem(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
 
    public RssItem() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
        return title;
    }
 
    public String getLink() {
        return link;
    }
    public String getDescrString() {
        return description;
    }
}