package net.rev.stealthandalert.attachment;

public record CrawlData(boolean isCrawling) {
    public static final CrawlData DEFAULT = new CrawlData(false);
}
