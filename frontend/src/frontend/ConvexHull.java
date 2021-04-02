package frontend;

import java.util.*;
import java.io.*;

public class ConvexHull {
    public class Point {
        public String name;
        public float rating;
        public float x;
        public float y;
        public Point (String name, float rating, float x, float y) {
            this.name = name;
            this.rating = rating;
            this.x = x;
            this.y = y;
        }
    }

    private ArrayList<Point> points;
    private ArrayList<Point> hull;
    public ConvexHull (String fileloc) throws IOException {
        points = new ArrayList<>();
        hull = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileloc))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(!line.equals("bus_name,bus_rating,loc_latitude,loc_longitude")) {
                    String[] values = line.split(",");
                    String name = values[0];
                    String rating = values[1];
                    String lat = values[2];
                    String lon = values[3];
                    points.add(new Point(name, Float.parseFloat(rating), Float.parseFloat(lon), Float.parseFloat(lat)));
                }
            }
        }
    }

    private int orientation (Point p, Point q, Point r) {
        float val = (((q.y - p.y)*(r.x - q.x)) - ((q.x - p.x)*(r.y - q.y)));
        if (val == 0.0) {
            return 0;
        }
        if (val > 0.0) {
            return 1;
        }
        return 2;
    }

    public List<Point> convexHull() {
        if (points.size() < 3) {
            return new ArrayList<>();
        }

        int left = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x < points.get(left).x) {
                left = i;
            }
        }

        int p = left;
        int q;
        do {
            hull.add(points.get(p));

            q = (p+1)%points.size();
            for (int i = 0; i < points.size(); i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                    q = i;
                }
            }

            p = q;
        }
        while (p != left);

        return hull;
    }

    public String toHullString() {
        StringBuilder ret = new StringBuilder();
		ret.append("<html><table border=1>");
        
        ArrayList<Point> hullTemp = new ArrayList<>();
        for (Point p : hull) {
            boolean name = false;
            for (Point po : hullTemp) {
                name = (name || p.name.equals(po.name));
            }
            if (!name) {
                hullTemp.add(p);
            }
        }

        ArrayList<Point> hullFive = new ArrayList<>();
        int index = 0;
        while (hullFive.size() < 5 && index < hullTemp.size()) {
            hullFive.add(hullTemp.get(index));
            if (hullTemp.size() <= 5) {
                index++;
            }
            else {
                index += Math.floorDiv(hullTemp.size(), 5);
            }
        }

		ret.append("<tr>");
		ret.append("<th align=\'center\'>Franchise Name</th>");
		ret.append("<th align=\'center\'>Rating</th>");
		ret.append("<th align=\'center\'>Latitude</th>");
		ret.append("<th align=\'center\'>Longitude</th>");
		ret.append("</tr>");
		
		for (int i = 0; i < hullFive.size(); i++) {
			ret.append("<tr>");
			ret.append("<td align=\'right\'>" + hullFive.get(i).name + "</td>");
			ret.append("<td align=\'center\'>" + hullFive.get(i).rating + "</td>");
			ret.append("<td align=\'left\'>" + hullFive.get(i).y + "</td>");
			ret.append("<td align=\'left\'>" + hullFive.get(i).x + "</td>");
			ret.append("</tr>");
		}
		
		ret.append("</table></html>");
		return ret.toString();
    }
}
