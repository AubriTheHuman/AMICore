package com.aubrithehuman.amicore.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class StabilityRegistry<T> extends JsonReloadListener {
	
//	public final List<StabilityObject> stabilityObjects = new ArrayList<StabilityObject>();
	
	
	// default gson if unspecified
	private static final Gson STANDARD_GSON = new Gson();
	
	/** The codec we use to convert jsonelements to Ts **/
	private final Codec<T> codec;
	
	/** Logger that will log data parsing errors **/
	private final Logger logger;
	
	private final String folderName;
	
	/** The raw data that we parsed from json last time resources were reloaded **/
	protected Map<ResourceLocation, T> data = new HashMap<>();
	
	/**
	 * Creates a data manager with a standard gson parser
	 * @param folderName The name of the data folder that we will load from, vanilla folderNames are "recipes", "loot_tables", etc</br>
	 * Jsons will be read from data/all_modids/folderName/all_jsons</br>
	 * folderName can include subfolders, e.g. "some_mod_that_adds_lots_of_data_loaders/cheeses"
	 * @param codec A codec to deserialize the json into your T, see javadocs above class
	 * @param logger A logger that will log json parsing problems when they are caught.
	 */
	public StabilityRegistry(String folderName, Codec<T> codec, Logger logger)
	{
		this(folderName, codec, logger, STANDARD_GSON);
	}
	
	/**
	 * As above but with a custom GSON
	 * @param folderName The name of the data folder that we will load from, vanilla folderNames are "recipes", "loot_tables", etc</br>
	 * Jsons will be read from data/all_modids/folderName/all_jsons</br>
	 * folderName can include subfolders, e.g. "some_mod_that_adds_lots_of_data_loaders/cheeses"
	 * @param codec A codec to deserialize the json into your T, see javadocs above class
	 * @param logger A logger that will log json parsing problems when they are caught.
	 * @param gson A gson for parsing the raw json data into JsonElements. JsonElement-to-T conversion will be done by the codec,
	 * so gson type adapters shouldn't be necessary here
	 */
	public StabilityRegistry(String folderName, Codec<T> codec, Logger logger, Gson gson)
	{
		super(gson, folderName);
		this.folderName = folderName; // superclass has this but it's a private field
		this.codec = codec;
		this.logger = logger;
	}
	
	/**
	 * Get the data object for the given key
	 * @param id A resourcelocation identifying a json; e.g. a json at data/some_modid/folderName/some_json.json has id "some_modid:some_json"
	 * @return The java object that was deserializd from the json with the given ID, or null if no such object is associated with that ID
	 */
	@Nullable
	public T getData(ResourceLocation id)
	{
		return this.data.get(id);
	}
	
	@Nullable
	public Collection<T> getData()
	{
		return this.data.values();
	}
	
	public boolean hasKey(ResourceLocation id) {
		return this.data.containsKey(id);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsons, IResourceManager resourceManager, IProfiler profiler)
	{
		this.logger.info("Beginning loading of data for data loader: {}", this.folderName);
		this.data = this.mapValues(jsons);
//		this.logger.debug(data.keySet());
//		this.logger.debug(data.entrySet().toArray().toString());
		this.logger.info("Data loader for {} loaded {} jsons", this.folderName, this.data.size());
	}

	private Map<ResourceLocation, T> mapValues(Map<ResourceLocation, JsonElement> inputs)
	{
		Map<ResourceLocation, T> newMap = new HashMap<>();

		for (Entry<ResourceLocation, JsonElement> entry : inputs.entrySet())
		{
			ResourceLocation key = entry.getKey();
			JsonElement element = entry.getValue();
			// if we fail to parse json, log an error and continue
			// if we succeeded, add the resulting T to the map
			this.codec.decode(JsonOps.INSTANCE, element)
				.get()
				.ifLeft(result -> newMap.put(key, result.getFirst()))
				.ifRight(partial -> this.logger.error("Failed to parse data json for {} due to: {}", key.toString(), partial.message()));
		}

		return newMap;
	}
	
   
	
//	protected static StabilityObject fromJSON(@Nullable JsonElement json) {
//		return null;
//	}
	
	
//		
//	public StabilityObject fromJson(ResourceLocation recipeId, JsonObject json) {
//		
//		String block = JSONUtils.getAsString(json, "block");
//		
//		double factor = JSONUtils.getAsFloat(json, "factor");
//
//		return new StabilityObject(block, factor);
//	}

	
}
