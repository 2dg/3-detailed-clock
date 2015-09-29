import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class Clock extends JPanel {

	protected static List<Clock> clockSync = new ArrayList<>();
	protected static Timer updater = new Timer(1000, actionEvent -> {
		clockSync.forEach(Component::repaint);
	});

	protected static int NORMAL = 30;
	protected static Font openSans = new Font("Open Sans", Font.PLAIN, NORMAL);

	protected static Stroke thinStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	protected static Stroke mediumStroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	protected static Stroke heavyStroke = new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	protected static Color blueGray = Color.decode("#212739");
	protected static Color trendyGreen = Color.decode("#1bce7c");
	protected static Color trendyRed = Color.decode("#ce3c1b");
	protected static Color white = Color.decode("#ffffff");

	protected static int radius = 250;

	protected TimeZone timezone;

	static {
		updater.start();
	}

	/**
	 * Creates a new Clock component synchronized to a timezone.
	 * @param timezone
	 */
	public Clock(String timezone) {
		clockSync.add(this);
		this.timezone = TimeZone.getTimeZone(timezone);
	}

	/**
	 * Creates a new Clock component synchronized to the system's timezone.
	 */
	public Clock() {
		this(ZoneId.systemDefault().getId());
	}

	/**
	 * Paints the frame, ticks, hands, and digital display for the clock.
	 * @param gg Graphics object
	 */
	@Override
	public void paint(Graphics gg) {
		super.paint(gg);

		// Fill background
		Graphics2D g = (Graphics2D)(gg);
		g.setColor(blueGray);
		g.fill(g.getClip());

		paintHourTicks(gg);
		paintOutline(gg);
		paintHands(gg);
		paintDigital(gg);
	}

	protected void paintOutline(Graphics gg) {
		Graphics2D g = (Graphics2D)(gg.create());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(50, 50);

		g.setColor(trendyGreen);
		g.setStroke(mediumStroke);

		g.drawArc(0, 0, 500, 500, 0, 360);
	}

	protected void paintHourTicks(Graphics gg) {
		Graphics2D g = (Graphics2D)(gg.create());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(50 + radius, 50 + radius);
		g.setFont(openSans);
		g.setStroke(mediumStroke);

		FontMetrics fm = g.getFontMetrics();

		for (int i = 0; i < 12; i++) {
			g.setColor(trendyGreen);
			double angle = Math.PI * (i - 3) / 6;
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);

			// draw tick
			g.drawLine(
					(int)(cos * (radius - NORMAL)), (int)(sin * (radius - NORMAL)),
					(int)(cos * radius), (int)(sin * radius)
			);

			// draw hour number
			g.setColor(white);
			String n = hourToString(i);
			g.drawString(
					n,
					(int)(cos * (radius - 2 * NORMAL)) - fm.stringWidth(n) / 2,
					(int)(sin * (radius - 2 * NORMAL)) - fm.getHeight() / 2 + fm.getAscent()
			);
		}
	}

	protected static String hourToString(int hour) {
		return String.valueOf(hour == 0 ? 12 : hour);
	}

	protected void paintHands(Graphics gg) {
		Graphics2D g = (Graphics2D)(gg.create());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(50 + radius, 50 + radius);

		Calendar calendar = GregorianCalendar.getInstance(timezone);

		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		// draw hour hand
		g.setStroke(heavyStroke);
		g.setColor(trendyRed);
		double hourAngle = Math.PI * (hour - 3) / 6;
		g.drawLine(
				0, 0,
				(int)(Math.cos(hourAngle) * (radius - 3 * NORMAL)),
				(int)(Math.sin(hourAngle) * (radius - 3 * NORMAL))
		);

		// draw minute hand
		g.setStroke(mediumStroke);
		g.setColor(white);
		double minuteAngle = Math.PI * (minute - 15) / 30;
		g.drawLine(
				0, 0,
				(int)(Math.cos(minuteAngle) * (radius - 2 * NORMAL)),
				(int)(Math.sin(minuteAngle) * (radius - 2 * NORMAL))
		);

		// draw second hand
		g.setStroke(thinStroke);
		g.setColor(trendyGreen);
		double secondAngle = Math.PI * (second - 15) / 30;
		g.drawLine(
				0, 0,
				(int)(Math.cos(secondAngle) * (radius - NORMAL)),
				(int)(Math.sin(secondAngle) * (radius - NORMAL))
		);

	}

	protected void paintDigital(Graphics gg) {
		Graphics2D g = (Graphics2D)(gg.create());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(openSans);
		g.setColor(white);
		FontMetrics fm = g.getFontMetrics();

		Calendar calendar = GregorianCalendar.getInstance(timezone);

		DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy HH:mm:ss zzz");
		dateFormat.setTimeZone(calendar.getTimeZone());
		String timeString = dateFormat.format(calendar.getTime());

		g.drawString(
				timeString,
				300 - fm.stringWidth(timeString) / 2,
				650 - fm.getHeight() / 2
		);
	}

}
