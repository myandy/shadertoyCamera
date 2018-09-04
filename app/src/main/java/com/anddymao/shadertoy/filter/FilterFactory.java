package com.anddymao.shadertoy.filter;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.filterlibrary.FilterSDK;

import java.util.ArrayList;

public class FilterFactory {

    public static ArrayList<FilterItem> getPortraitFilterItem() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        filters.add(new FilterItem(null, FilterSDK.sContext.getString(R.string.filter_original), R.drawable.filter_people_original, 0));
        filters.add(new FilterItem(R.raw.fokkkus, "fokkkus", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.droste2, "droste2", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.ripple_distortion, "ripple_distortion", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.edge, "edge", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.acid_party, "acid_party", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.carvo, "carvo", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.coy_pond, "coy_pond", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.distance, "distance", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.dithering, "dithering", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.droste, "droste", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.drunkdial, "drunkdial", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.feel_sceeen_ba, "feel_sceeen_ba", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.feel_sceeen_ba1, "feel_sceeen_ba1", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.glitch, "glitch", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.hotlinemiami2, "hotlinemiami2", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.intensity, "intensity", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.intergalactic, "intergalactic", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.kaleidoscope, "kaleidoscope", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.liquid, "liquid", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.matrix88, "matrix88", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.ngmir1, "ngmir1", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.ngmir4, "ngmir4", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.pixelshining, "pixelshining", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.predator, "predator", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.rainbows, "rainbows", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.refraction, "refraction", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.ripple, "ripple", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.slices, "slices", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.snapshots, "snapshots", R.drawable.filter_people_nature));
        filters.add(new FilterItem(R.raw.timoh, "timoh", R.drawable.filter_people_nature));
        filters.add(new FilterItem(FilterItem.LUT_ADORE, FilterSDK.sContext.getString(R.string.filter_adore), R.drawable.filter_people_nature, 70));
        return filters;
    }
}
