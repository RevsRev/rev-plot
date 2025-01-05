package github.com.rev.plot.graphics;

import github.com.rev.plot.canvas.ScreenCoordinateMapper;
import lombok.Getter;
import lombok.Setter;
import rev.pe.math.linear.vec.Vec2;

import java.awt.Color;
import java.awt.Graphics;


@Setter
public abstract class Stylus {
    private Graphics g = null;

    private final ScreenCoordinateMapper coordinateMapper;

    @Getter @Setter
    private Color backgroundColor = Color.WHITE;
    @Getter @Setter
    private Color defaultLineColor = Color.BLACK;

    @Getter
    private boolean erase = false;

    protected Stylus(final ScreenCoordinateMapper coordinateMapper) {
        this.coordinateMapper = coordinateMapper;
    }

    public abstract Vec2 transform(Vec2 point);

    public final void drawPoint(final Vec2 point, final int size) {
        if (g == null) {
            return;
        }

        Vec2 p = transform(point);
        mapToScreen(p);
        draw(() -> g.fillOval((int) p.x - size / 2, (int) p.y - size / 2, size, size));
    }

    public final void drawLine(final Vec2 start, final Vec2 end) {
        if (g == null) {
            return;
        }
        Vec2 s = transform(start);
        Vec2 e = transform(end);
        mapToScreen(s);
        mapToScreen(e);

        draw(() -> g.drawLine((int) s.x, (int) s.y, (int) e.x, (int) e.y));
    }

    public final void fillRectangle(final double x,
                              final double y,
                              final double width,
                              final double height) {
        Vec2 bottomLeftCorner = new Vec2(x, y);
        Vec2 topRightCorner = new Vec2(x + width, y + height);
        mapToScreen(bottomLeftCorner);
        mapToScreen(topRightCorner);
        draw(() -> g.fillRect((int) bottomLeftCorner.x, (int) bottomLeftCorner.y,
                (int) (topRightCorner.x - bottomLeftCorner.x), (int) (bottomLeftCorner.y - topRightCorner.y)));
    }

    private Runnable getDrawableRunnable(final Runnable implementationRunnable) {
        return () -> {
            if (erase) {
                g.setColor(backgroundColor);
            }
            implementationRunnable.run();
            if (erase) {
                g.setColor(defaultLineColor);
            }
        };
    }

    private void draw(final Runnable drawImplementation) {
        Runnable drawRunnable = getDrawableRunnable(drawImplementation);
        drawRunnable.run();
    }

    private void mapToScreen(final Vec2 p) {
        coordinateMapper.mapToScreen(p);
    }

    public final void setColor(final Color color) {
        if (g != null) {
            g.setColor(color);
        }
    }
    public final Color getColor() {
        if (g != null) {
            return g.getColor();
        }
        return null;
    }
}