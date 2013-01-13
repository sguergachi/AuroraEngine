/*
 * Copyright © 2008 - 2012 Lars Vogel
 */
package aurora.engine.V1.Logic;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Version 2.1
 * Combines Feed, FeedMessage and RSSParser into one class
 * <p/>
 * @author Lars Vogel
 */
public class ARssReader {

    /*
     * Stores an RSS feed
     */
    public class Feed {

        final String title;

        final String link;

        final String description;

        final String language;

        final String copyright;

        final String pubDate;

        final List<FeedMessage> entries = new ArrayList<FeedMessage>();

        public Feed(String title, String link, String description,
                    String language,
                    String copyright, String pubDate) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.language = language;
            this.copyright = copyright;
            this.pubDate = pubDate;
        }

        public List<FeedMessage> getMessages() {
            return entries;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        public String getLanguage() {
            return language;
        }

        public String getCopyright() {
            return copyright;
        }

        public String getPubDate() {
            return pubDate;
        }

        @Override
        public String toString() {
            return "Feed [copyright=" + copyright + ", description="
                   + description
                   + ", language=" + language + ", link=" + link + ", pubDate="
                   + pubDate + ", title=" + title + "]";
        }
    }

    /*
     * Represents one RSS message
     */
    public class FeedMessage {

        String title;

        String description;

        String link;

        String author;

        String guid;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        /* @Override
         public String toString() {
         return "FeedMessage [title=" + title + ", description=" + description
         + ", link=" + link + ", author=" + author + ", guid=" + guid
         + "]";
         }*/
        @Override
        public String toString() {
            return title;
        }
    }

    public class RSSFeedParser {

        static final String TITLE = "title";

        static final String DESCRIPTION = "description";

        static final String CHANNEL = "channel";

        static final String LANGUAGE = "language";

        static final String COPYRIGHT = "copyright";

        static final String LINK = "link";

        static final String AUTHOR = "author";

        static final String ITEM = "item";

        static final String PUB_DATE = "pubDate";

        static final String GUID = "guid";

        final URL url;

        public RSSFeedParser(String feedUrl) {
            try {
                this.url = new URL(feedUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        @SuppressWarnings("null")
        public Feed readFeed() {
            Feed feed = null;
            try {

                boolean isFeedHeader = true;
                // Set header values initially to the empty string
                String description = "";
                String title = "";
                String link = "";
                String language = "";
                String copyright = "";
                String author = "";
                String pubdate = "";
                String guid = "";

                // First create a new XMLInputFactory
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                // Setup a new eventReader
                InputStream in = read();
                XMLEventReader eventReader = inputFactory.createXMLEventReader(
                        in);
                // Read the XML document
                while (eventReader.hasNext()) {

                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                            == (ITEM)) {
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description,
                                        language,
                                        copyright, pubdate);
                            }
                            event = eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart()
                            == (TITLE)) {
                            event = eventReader.nextEvent();
                            title = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart()
                            == (DESCRIPTION)) {
                            event = eventReader.nextEvent();
                            description = event.asCharacters().getData();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart()
                            == (LINK)) {
                            event = eventReader.nextEvent();
                            link = event.asCharacters().getData();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart()
                            == (GUID)) {
                            event = eventReader.nextEvent();
                            guid = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart()
                            == (LANGUAGE)) {
                            event = eventReader.nextEvent();
                            language = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart()
                            == (AUTHOR)) {
                            event = eventReader.nextEvent();
                            author = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart()
                            == (PUB_DATE)) {
                            event = eventReader.nextEvent();
                            pubdate = event.asCharacters().getData();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart()
                            == (COPYRIGHT)) {
                            event = eventReader.nextEvent();
                            copyright = event.asCharacters().getData();
                            continue;
                        }
                    } else if (event.isEndElement()) {
                        if (event.asEndElement().getName().getLocalPart()
                            == (ITEM)) {
                            FeedMessage message = new FeedMessage();
                            message.setAuthor(author);
                            message.setDescription(description);
                            message.setGuid(guid);
                            message.setLink(link);
                            message.setTitle(title);
                            feed.getMessages().add(message);
                            event = eventReader.nextEvent();
                            continue;
                        }
                    }
                }
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }
            return feed;

        }

        private InputStream read() {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}