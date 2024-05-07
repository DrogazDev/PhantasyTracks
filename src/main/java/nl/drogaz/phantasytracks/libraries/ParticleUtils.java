package nl.drogaz.phantasytracks.libraries;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleUtils {

    public static void spawnSmoothCurveParticles(Location start, Location end, int maxSteps, Particle particleType, Color particleColor) {
        for (int j = 0; j <= maxSteps; j++) {
            double t = (double) j / maxSteps;

            // Smooth interpolation for x, y, z
            double x = lerp(start.getX(), end.getX(), t);
            double y = smoothInterpolation(start.getY(), end.getY(), t);
            double z = lerp(start.getZ(), end.getZ(), t);

            start.getWorld().spawnParticle(particleType, new Location(start.getWorld(), x, y, z), 1, 0, 0, 0, 0, new Particle.DustOptions(particleColor, 1));
        }
    }

    private static double lerp(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }

    private static double smoothInterpolation(double start, double end, double t) {
        // Smooth interpolation function (you can adjust this function for different effects)
        return start + (end - start) * ((t * t) * (3 - 2 * t));
    }

    public static void spawnQuadraticBezierCurveParticles(Location currentNodeLoc, Location controlNodeLoc, Location nextNodeLoc, int maxSteps, Particle particleType, Color particleColor) {
        for (int j = 0; j <= maxSteps; j++) {
            double t = (double) j / maxSteps;

            // Quadratic Bezier curve interpolation for x, y, z
            double x = quadraticBezier(currentNodeLoc.getX(), controlNodeLoc.getX(), nextNodeLoc.getX(), t);
            double y = quadraticBezier(currentNodeLoc.getY(), controlNodeLoc.getY(), nextNodeLoc.getY(), t);
            double z = quadraticBezier(currentNodeLoc.getZ(), controlNodeLoc.getZ(), nextNodeLoc.getZ(), t);

            currentNodeLoc.getWorld().spawnParticle(particleType, new Location(currentNodeLoc.getWorld(), x, y, z), 1, 0, 0, 0, 0, new Particle.DustOptions(particleColor, 1));
        }
    }

    private static double quadraticBezier(double p0, double p1, double p2, double t) {
        // Quadratic Bezier curve formula
        return Math.pow(1 - t, 2) * p0 + 2 * (1 - t) * t * p1 + Math.pow(t, 2) * p2;
    }

}
