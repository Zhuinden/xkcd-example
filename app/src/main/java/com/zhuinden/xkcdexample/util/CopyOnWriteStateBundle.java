package com.zhuinden.xkcdexample.util;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.zhuinden.statebundle.StateBundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by Zhuinden on 2017.04.14..
 */

public class CopyOnWriteStateBundle
        extends StateBundle {
    public CopyOnWriteStateBundle() {
    }

    public CopyOnWriteStateBundle(StateBundle stateBundle) {
        super(stateBundle);
    }

    /**
     * Copies the keys of the bundle.
     *
     * @param bundle the other bundle
     * @return the bundle copied and wrapped as a CopyOnWriteStateBundle
     */
    public StateBundle putAll(StateBundle bundle) {
        return new CopyOnWriteStateBundle(new StateBundle(bundle));
    }

    /**
     * Clears the bundle.
     *
     * @return an empty state bundle wrapped as CopyOnWriteStateBundle
     */
    @Override
    public StateBundle clear() {
        return new CopyOnWriteStateBundle(new StateBundle());
    }

    /**
     * Returns a Set containing the Strings used as keys in this Bundle.
     *
     * @return a Set of String keys
     */
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    /**
     * Inserts a Boolean value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a boolean
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putBoolean(@Nullable String key, boolean value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putBoolean(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a byte value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a byte
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putByte(@Nullable String key, byte value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putByte(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a char value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a char
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putChar(@Nullable String key, char value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putChar(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a short value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a short
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putShort(@Nullable String key, short value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putShort(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts an int value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value an int
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putInt(@Nullable String key, int value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putInt(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a long value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a long
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putLong(@Nullable String key, long value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putLong(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a float value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a float
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putFloat(@Nullable String key, float value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putFloat(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a double value into the map of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a double
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putDouble(@Nullable String key, double value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putDouble(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a String value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a String, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putString(@Nullable String key, @Nullable String value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putString(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a CharSequence value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a CharSequence, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putCharSequence(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts an ArrayList of Integer value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList of Integer object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putIntegerArrayList(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts an ArrayList of String value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList of String object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putStringArrayList(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts an ArrayList of CharSequence value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList of CharSequence object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putCharSequenceArrayList(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a Serializable value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Serializable object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putSerializable(@Nullable String key, @Nullable Serializable value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putSerializable(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a boolean array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a boolean array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putBooleanArray(@Nullable String key, @Nullable boolean[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putBooleanArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a byte array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a byte array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putByteArray(@Nullable String key, @Nullable byte[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putByteArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a short array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a short array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putShortArray(@Nullable String key, @Nullable short[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putShortArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a char array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a char array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putCharArray(@Nullable String key, @Nullable char[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putCharArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts an int array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value an int array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putIntArray(@Nullable String key, @Nullable int[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putIntArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a long array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a long array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putLongArray(@Nullable String key, @Nullable long[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putLongArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a float array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a float array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putFloatArray(@Nullable String key, @Nullable float[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putFloatArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a double array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a double array object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putDoubleArray(@Nullable String key, @Nullable double[] value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putDoubleArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a String array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a String array object, or null 
     @return a new bundle wrapped as CopyOnWriteStateBundle
     */
//    public StateBundle putStringArray(@Nullable String key, @Nullable String[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

    /**
     * Inserts a CharSequence array value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a CharSequence array object, or null 
     @return a new bundle wrapped as CopyOnWriteStateBundle
     */
//    public StateBundle putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }


    /**
     * Inserts a Parcelable value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Parcelable object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putParcelable(@Nullable String key, @Nullable Parcelable value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putParcelable(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a Size value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Size object, or null 
     @return a new bundle wrapped as CopyOnWriteStateBundle
     */
//    public StateBundle putSize(@Nullable String key, @Nullable Size value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

    /**
     * Inserts a SizeF value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a SizeF object, or null 
     @return a new bundle wrapped as CopyOnWriteStateBundle
     */
//    public StateBundle putSizeF(@Nullable String key, @Nullable SizeF value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

    /**
     * Inserts an array of Parcelable values into the map of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key   a String, or null
     * @param value an array of Parcelable objects, or null 
     @return a new bundle wrapped as CopyOnWriteStateBundle
     */
//    public StateBundle putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

    /**
     * Inserts a List of Parcelable values into the map of this Bundle,
     * replacing any existing value for the given key.  Either key or value may
     * be null.
     *
     * @param key   a String, or null
     * @param value an ArrayList of Parcelable objects, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putParcelableArrayList(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a SparceArray of Parcelable values into the map of this
     * Bundle, replacing any existing value for the given key.  Either key
     * or value may be null.
     *
     * @param key   a String, or null
     * @param value a SparseArray of Parcelable objects, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putSparseParcelableArray(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    /**
     * Inserts a Bundle value into the map of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Bundle object, or null
     * @return a new bundle wrapped as CopyOnWriteStateBundle
     */
    public StateBundle putBundle(@Nullable String key, @Nullable StateBundle value) {
        StateBundle stateBundle = new StateBundle(this);
        stateBundle.putBundle(key, value);
        return new CopyOnWriteStateBundle(stateBundle);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        StateBundle stateBundle = new StateBundle();

        private Builder() {
        }

        /**
         * Removes all elements from the map of this Bundle.
         *
         * @return this builder
         */
        public Builder clear() {
            stateBundle.clear();
            return this;
        }

        /**
         * Returns true if the given key is contained in the map
         * of this Bundle.
         *
         * @param key a String key
         * @return true if the key is part of the map, false otherwise
         */
        public boolean containsKey(String key) {
            return stateBundle.containsKey(key);
        }

        /**
         * Returns the entry with the given key as an object.
         *
         * @param key a String key
         * @return an Object, or null
         */
        @Nullable
        public Object get(String key) {
            return stateBundle.get(key);
        }

        /**
         * Removes any entry with the given key from the map of this Bundle.
         *
         * @param key a String key
         * @return this builder
         */
        public Builder remove(String key) {
            stateBundle.remove(key);

            return this;
        }

        /**
         * Inserts all maps from the given StateBundle into this StateBundle.
         *
         * @param bundle a {@link StateBundle}
         * @return this builder
         */
        public Builder putAll(StateBundle bundle) {
            if(bundle != null) {
                stateBundle.putAll(bundle);
            }
            return this;
        }

        /**
         * Returns a Set containing the Strings used as keys in this Bundle.
         *
         * @return a Set of String keys
         */
        public Set<String> keySet() {
            return stateBundle.keySet();
        }

        /**
         * Inserts a Boolean value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a boolean
         * @return this builder
         */
        public Builder putBoolean(@Nullable String key, boolean value) {
            stateBundle.putBoolean(key, value);
            return this;
        }

        /**
         * Inserts a byte value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a byte
         * @return this builder
         */
        public Builder putByte(@Nullable String key, byte value) {
            stateBundle.putByte(key, value);
            return this;
        }

        /**
         * Inserts a char value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a char
         * @return this builder
         */
        public Builder putChar(@Nullable String key, char value) {
            stateBundle.putChar(key, value);
            return this;
        }

        /**
         * Inserts a short value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a short
         * @return this builder
         */
        public Builder putShort(@Nullable String key, short value) {
            stateBundle.putShort(key, value);
            return this;
        }

        /**
         * Inserts an int value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value an int
         * @return this builder
         */
        public Builder putInt(@Nullable String key, int value) {
            stateBundle.putInt(key, value);
            return this;
        }

        /**
         * Inserts a long value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a long
         * @return this builder
         */
        public Builder putLong(@Nullable String key, long value) {
            stateBundle.putLong(key, value);
            return this;
        }

        /**
         * Inserts a float value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a float
         * @return this builder
         */
        public Builder putFloat(@Nullable String key, float value) {
            stateBundle.putFloat(key, value);
            return this;
        }

        /**
         * Inserts a double value into the map of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a double
         * @return this builder
         */
        public Builder putDouble(@Nullable String key, double value) {
            stateBundle.putDouble(key, value);
            return this;
        }

        /**
         * Inserts a String value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a String, or null
         * @return this builder
         */
        public Builder putString(@Nullable String key, @Nullable String value) {
            stateBundle.putString(key, value);
            return this;
        }

        /**
         * Inserts a CharSequence value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a CharSequence, or null
         * @return this builder
         */
        public Builder putCharSequence(@Nullable String key, @Nullable CharSequence value) {
            stateBundle.putCharSequence(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList of Integer value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList of Integer object, or null
         * @return this builder
         */
        public Builder putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
            stateBundle.putIntegerArrayList(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList of String value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList of String object, or null
         * @return this builder
         */
        public Builder putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
            stateBundle.putStringArrayList(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList of CharSequence value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList of CharSequence object, or null
         * @return this builder
         */
        public Builder putCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
            stateBundle.putCharSequenceArrayList(key, value);
            return this;
        }

        /**
         * Inserts a Serializable value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Serializable object, or null
         * @return this builder
         */
        public Builder putSerializable(@Nullable String key, @Nullable Serializable value) {
            stateBundle.putSerializable(key, value);
            return this;
        }

        /**
         * Inserts a boolean array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a boolean array object, or null
         * @return this builder
         */
        public Builder putBooleanArray(@Nullable String key, @Nullable boolean[] value) {
            stateBundle.putBooleanArray(key, value);
            return this;
        }

        /**
         * Inserts a byte array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a byte array object, or null
         * @return this builder
         */
        public Builder putByteArray(@Nullable String key, @Nullable byte[] value) {
            stateBundle.putByteArray(key, value);
            return this;
        }

        /**
         * Inserts a short array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a short array object, or null
         * @return this builder
         */
        public Builder putShortArray(@Nullable String key, @Nullable short[] value) {
            stateBundle.putShortArray(key, value);
            return this;
        }

        /**
         * Inserts a char array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a char array object, or null
         * @return this builder
         */
        public Builder putCharArray(@Nullable String key, @Nullable char[] value) {
            stateBundle.putCharArray(key, value);
            return this;
        }

        /**
         * Inserts an int array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an int array object, or null
         * @return this builder
         */
        public Builder putIntArray(@Nullable String key, @Nullable int[] value) {
            stateBundle.putIntArray(key, value);
            return this;
        }

        /**
         * Inserts a long array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a long array object, or null
         * @return this builder
         */
        public Builder putLongArray(@Nullable String key, @Nullable long[] value) {
            stateBundle.putLongArray(key, value);
            return this;
        }

        /**
         * Inserts a float array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a float array object, or null
         * @return this builder
         */
        public Builder putFloatArray(@Nullable String key, @Nullable float[] value) {
            stateBundle.putFloatArray(key, value);
            return this;
        }

        /**
         * Inserts a double array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a double array object, or null
         * @return this builder
         */
        public Builder putDoubleArray(@Nullable String key, @Nullable double[] value) {
            stateBundle.putDoubleArray(key, value);
            return this;
        }

        /**
         * Inserts a String array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a String array object, or null
         * @return this builder
         */
//    public Builder putStringArray(@Nullable String key, @Nullable String[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

        /**
         * Inserts a CharSequence array value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a CharSequence array object, or null
         * @return this builder
         */
//    public Builder putCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }


        /**
         * Inserts a Parcelable value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Parcelable object, or null
         * @return this builder
         */
        public Builder putParcelable(@Nullable String key, @Nullable Parcelable value) {
            stateBundle.putParcelable(key, value);
            return this;
        }

        /**
         * Inserts a Size value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Size object, or null
         * @return this builder
         */
//    public Builder putSize(@Nullable String key, @Nullable Size value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

        /**
         * Inserts a SizeF value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a SizeF object, or null
         * @return this builder
         */
//    public Builder putSizeF(@Nullable String key, @Nullable SizeF value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

        /**
         * Inserts an array of Parcelable values into the map of this Bundle,
         * replacing any existing value for the given key.  Either key or value may
         * be null.
         *
         * @param key   a String, or null
         * @param value an array of Parcelable objects, or null
         * @return this builder
         */
//    public Builder putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
//        stateBundle.put(key, value);
//        
//        return this;
//    }

        /**
         * Inserts a List of Parcelable values into the map of this Bundle,
         * replacing any existing value for the given key.  Either key or value may
         * be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList of Parcelable objects, or null
         * @return this builder
         */
        public Builder putParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
            stateBundle.putParcelableArrayList(key, value);
            return this;
        }

        /**
         * Inserts a SparceArray of Parcelable values into the map of this
         * Bundle, replacing any existing value for the given key.  Either key
         * or value may be null.
         *
         * @param key   a String, or null
         * @param value a SparseArray of Parcelable objects, or null
         * @return this builder
         */
        public Builder putSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
            stateBundle.putSparseParcelableArray(key, value);
            return this;
        }

        /**
         * Inserts a Bundle value into the map of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Bundle object, or null
         * @return this builder
         */
        public Builder putBundle(@Nullable String key, @Nullable StateBundle value) {
            stateBundle.putBundle(key, value);
            return this;
        }
    }
}
