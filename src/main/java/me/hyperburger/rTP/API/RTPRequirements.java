package me.hyperburger.rTP.API;


import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing the requirements for random teleportation.
 */
public class RTPRequirements {
    private final Set<Biome> biomes = new HashSet<>(Arrays.asList(Biome.values()));
    private int minX = -1;
    private int maxX = -1;
    private int minZ = -1;
    private int maxZ = -1;

    /**
     * Specifies the required biomes.
     *
     * @param biomes The required biomes.
     * @return This {@link RTPRequirements} instance.
     */

    @CanIgnoreReturnValue
    public RTPRequirements require(final Biome ... biomes) {
        this.biomes.clear();
        this.biomes.addAll(new ArrayList<>(Arrays.asList(biomes)));
        return this;
    }

    /**
     * Specifies a required biome.
     *
     * @param biome The required biome.
     * @return This {@link RTPRequirements} instance.
     */

    @CanIgnoreReturnValue
    public RTPRequirements require(final Biome biome) {
        this.biomes.clear();
        this.biomes.add(biome);
        return this;
    }

    /**
     * Creates a new {@link RTPRequirements} instance.
     *
     * @return The created {@link RTPRequirements} instance.
     */
    @CanIgnoreReturnValue
    public static RTPRequirements requirements() {
        return new RTPRequirements();
    }

    /**
     * Retrieves the set of required biomes.
     *
     * @return The set of required biomes.
     */
    public Set<Biome> biomes() {
        return biomes;
    }

    /**
     * Specifies the required coordinate range.
     *
     * @param coordinate The coordinate (X or Z).
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @return This {@link RTPRequirements} instance.
     */
    @CanIgnoreReturnValue
    public RTPRequirements require(final String coordinate, final int minimum, final int maximum) {
        if (coordinate.equalsIgnoreCase("X")) {
            this.minX = minimum;
            this.maxX = maximum;
        } else {
            this.minZ = minimum;
            this.maxZ = maximum;
        }
        return this;
    }

    /**
     * Specifies the required coordinate range.
     *
     * @param coordinate The coordinate (X or Z).
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @return This {@link RTPRequirements} instance.
     */

    @CanIgnoreReturnValue
    public RTPRequirements require(final Coordinate coordinate, final int minimum, final int maximum) {
        return require(coordinate.name(), minimum, maximum);
    }

    /**
     * Specifies the required X coordinate range.
     *
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @return This {@link RTPRequirements} instance.
     */
     @CanIgnoreReturnValue
    public RTPRequirements requireX(final int minimum, final int maximum) {
        return require("X", minimum, maximum);
    }

    /**
     * Specifies the required Z coordinate range.
     *
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @return This {@link RTPRequirements} instance.
     */
    @CanIgnoreReturnValue
    public RTPRequirements requireZ(final int minimum, final int maximum) {
        return require("Z", minimum, maximum);
    }

    /**
     * Specifies the required coordinate ranges for both X and Z.
     *
     * @param minimumX The minimum X value.
     * @param maximumX The maximum X value.
     * @param minimumZ The minimum Z value.
     * @param maximumZ The maximum Z value.
     * @return This {@link RTPRequirements} instance.
     */
    @CanIgnoreReturnValue
    public RTPRequirements require(final int minimumX, final int maximumX, final int minimumZ, final int maximumZ) {
        return requireX(minimumX, maximumX).requireZ(minimumZ, maximumZ);
    }

    /**
     * Retrieves the minimum X coordinate.
     *
     * @return The minimum X coordinate.
     */
    public int minX() {
        return minX;
    }

    /**
     * Retrieves the maximum X coordinate.
     *
     * @return The maximum X coordinate.
     */
    public int maxX() {
        return maxX;
    }

    /**
     * Retrieves the minimum Z coordinate.
     *
     * @return The minimum Z coordinate.
     */
    public int minZ() {
        return minZ;
    }

    /**
     * Retrieves the maximum Z coordinate.
     *
     * @return The maximum Z coordinate.
     */
    public int maxZ() {
        return maxZ;
    }
}