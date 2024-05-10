package com.example.simulation2.structs;

/**
 * Represents a 2D vector.
 */
public class Vector2
{
    /**
     * The x component of the vector.
     */
    public float x;
    /**
     * The y component of the vector.
     */
    public float y;

    // Constants
    /**
     * The zero vector (0, 0).
     */
    public static final Vector2 Zero = new Vector2(0, 0);
    /**
     * The vector with components (1, 1).
     */
    public static final Vector2 One = new Vector2(1, 1);
    /**
     * The unit vector pointing upwards (0, 1).
     */
    public static final Vector2 Up = new Vector2(0, 1);
    /**
     * The unit vector pointing to the left (-1, 0).
     */
    public static final Vector2 Left = new Vector2(-1, 0);
    /**
     * The unit vector pointing downwards (0, -1).
     */
    public static final Vector2 Down = new Vector2(0, -1);
    /**
     * The unit vector pointing to the right (1, 0).
     */
    public static final Vector2 Right = new Vector2(1, 0);

    /**
     * Constructs a new Vector2 instance.
     *
     * @param x The x component
     * @param y The y component
     */
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x and y components of the vector.
     *
     * @param x The new x component
     * @param y The new y component
     */
    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The vector to add
     */
    public void add(Vector2 other)
    {
        this.x += other.x;
        this.y += other.y;
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The vector to subtract
     */
    public void subtract(Vector2 other)
    {
        this.x -= other.x;
        this.y -= other.y;
    }

    /**
     * Multiplies this vector by a scalar value.
     *
     * @param scalar The scalar value to multiply by
     */
    public void multiply(float scalar)
    {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Calculates the magnitude (length) of the vector.
     *
     * @return The magnitude of the vector
     */
    public float magnitude()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes the vector (sets its magnitude to 1).
     * If the vector is the zero vector, does nothing.
     */
    public void normalize()
    {
        float mag = magnitude();
        if (mag != 0)
        {
            this.x /= mag;
            this.y /= mag;
        }
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     * @return The dot product of the two vectors
     */
    public float dot(Vector2 other)
    {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates the distance between this vector and another vector.
     *
     * @param other The other vector
     * @return The distance between the two vectors
     */
    public float distance(Vector2 other)
    {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Gets the copy of this vector.
     *
     * @return The copy of this vector
     */
    public Vector2 copy()
    {
        return new Vector2(this.x, this.y);
    }

    /**
     * Duplicate the target Vector2 value as this Vector2 value.
     *
     * @param target The other vector
     */
    public void duplicate(Vector2 target)
    {
        this.x = target.x;
        this.y = target.y;
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A string representation of the vector
     */
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Adds two vectors and returns the result as a new vector.
     *
     * @param v1 The first vector
     * @param v2 The second vector
     * @return The sum of the two vectors
     */
    public static Vector2 add(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Subtracts one vector from another and returns the result as a new vector.
     *
     * @param v1 The vector to subtract from
     * @param v2 The vector to subtract
     * @return The difference of the two vectors
     */
    public static Vector2 subtract(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * Multiplies a vector by a scalar value and returns the result as a new vector.
     *
     * @param v      The vector to multiply
     * @param scalar The scalar value to multiply by
     * @return The result of the multiplication
     */
    public static Vector2 multiply(Vector2 v, float scalar)
    {
        return new Vector2(v.x * scalar, v.y * scalar);
    }

    /**
     * Divides a vector by a scalar value and returns the result as a new vector.
     *
     * @param v      The vector to divide
     * @param scalar The scalar value to divide by
     * @return The result of the division
     * @throws IllegalArgumentException if the scalar value is zero
     */
    public static Vector2 divide(Vector2 v, float scalar)
    {
        if (scalar == 0)
        {
            throw new IllegalArgumentException("Division by zero");
        }
        return new Vector2(v.x / scalar, v.y / scalar);
    }
}

