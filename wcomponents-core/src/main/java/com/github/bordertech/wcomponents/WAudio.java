package com.github.bordertech.wcomponents;

import com.github.bordertech.wcomponents.util.Util;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * WAudio provides a means to play audio content. For most uses this means a HTML audio element. If the client does not
 * implement the audio element or cannot play any of the supplied sources then an alternate means to access the sources
 * is provided.
 *</p>
 * <p>
 * Each WAudio component must have at least one {@link Audio} resource. Each such resource should be appropriate for
 * delivery over the web and in a format suitable for the application's target browsers. If the application has a
 * mixed browser matrix then it may be appropriate to attach multiple sources to each WAudio. It is <strong>strongly
 * recommended</strong> that AVI files are <strong>never</strong> used as an Audio resource.
 * </p>
 * <p>
 * Every use of WAudio <strong>must</strong> comply with the requirements outlined in
 * <a href="https://www.w3.org/TR/media-accessibility-reqs/">Media Accessibility User Requirements</a>, and meet
 * guidelines
 * <a href="https://www.w3.org/WAI/WCAG20/quickref/#media-equiv">1.2</a>,
 * <a href="https://www.w3.org/WAI/WCAG20/quickref/#visual-audio-contrast-dis-audio">1.4.2</a> and
 * <a href="https://www.w3.org/WAI/WCAG20/quickref/#visual-audio-contrast-noaudio">1.4.7</a>.
 * </p>
 *
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class WAudio extends AbstractWComponent implements Targetable, AjaxTarget, Disableable {

	/**
	 * The logger instance for this class.
	 */
	private static final Log LOG = LogFactory.getLog(WAudio.class);

	/**
	 * This request parameter is used to determine which audio clip to serve up.
	 */
	private static final String AUDIO_INDEX_REQUEST_PARAM_KEY = "WAudio.index";

	/**
	 * This is used to indicate whether pre-loading of content should occur before the clip is played. If the audio
	 * clip is a static, cacheable resource then Preload.AUTO is highly recommended. If the clip is not cacheable and
	 * is streamed from the application's resources the default value of Preload.NONE should usually be used.
	 */
	public enum Preload {
		/**
		 * Do not pre-load any data. This is best for clips streamed from the application's resources or live feeds.
		 */
		NONE,
		/**
		 * Preload meta-data only.
		 */
		META_DATA,
		/**
		 * Let the client determine what to load. This is usually best for pre-recorded audio presented as cacheable
		 * static resources.
		 */
		AUTO
	}

	/**
	 * This is used to indicate which playback controls to display for the audio.
	 *
	 * <p>
	 * <strong>Note:</strong>
	 * Advancements in audio support in clients since this API was first implemented means that most of this is now
	 * redundant. Under most circumstances the UI will display their native audio controls. Where a particular WAudio
	 * does not have any source which is able to be played by the client then links to all sources will be provided.
	 * This enum is not worthless as the values NONE and PLAY_PAUSE are used to turn off native audio controls in the
	 * client. The value NONE however causes major problems and is incompatible with autoplay for a11y reasons so it
	 * basically makes the media worthless. This enum may be replaced in the future with a simple boolean to trigger
	 * native controls or play/pause only (see https://github.com/BorderTech/wcomponents/issues/503).
	 * </p>
	 */
	public enum Controls {
		/**
		 * Do not display any controls. May be incompatible with either {@link #isAutoplay()} == true or
		 * {@link #isLoop()} == true. If this is set then the WAudio control <strong>MAY NOT WORK</strong>.
		 * @deprecated since 1.1.1 as this is incompatible with WCAG requirements.
		 */
		NONE,
		/**
		 * Display all controls. What this actually means depends upon the theme.
		 * @deprecated since 1.1.1 as themes use native controls.
		 */
		ALL,
		/**
		 * A combined play/pause button.
		 */
		PLAY_PAUSE,
		/**
		 * Displays the "default" set of controls for the theme.
		 * @deprecated since 1.1.1 as themes use native audio controls.
		 */
		DEFAULT,
		/**
		 * Displays the client's native set of controls.
		 */
		NATIVE
	}

	/**
	 * Creates a WAudio with no audio clips. Audio clips must be added later by calling one of the setAudio(...)
	 * methods.
	 */
	public WAudio() {
	}

	/**
	 * Creates a WAudio with a given audio clip.
	 *
	 * @param audio the audio clip
	 */
	public WAudio(final Audio audio) {
		this(new Audio[]{audio});
	}

	/**
	 * <p>
	 * Creates a WAudio with the given static content. This is provided as a convenience method for when the audio file
	 * is included as static content in the class path rather than in the web application's resources.
	 * </p>
	 * <p>
	 * The mime type for the audio clip is looked up from the "mimeType.*" mapping configuration parameters using the
	 * resource's file extension.
	 * </p>
	 *
	 * @param resource the resource path to the audio file
	 */
	public WAudio(final String resource) {
		this(new AudioResource(resource));
	}

	/**
	 * Creates a WAudio with the given audio clip in multiple formats. The client will try to load the first audio clip,
	 * and if it fails or isn't supported, it will move on to the next audio clip. Only the first clip which can be
	 * played on the client will be used.
	 *
	 * @param audio multiple formats for the same the audio clip
	 */
	public WAudio(final Audio[] audio) {
		setAudio(audio);
	}

	/**
	 * Sets the audio clip for all users.
	 *
	 * @param audio the audio clip
	 */
	public void setAudio(final Audio audio) {
		setAudio(new Audio[]{audio});
	}

	/**
	 * Sets the audio clip in multiple formats for all users. The client will try to load the first audio clip, and if
	 * it fails or isn't supported, it will move on to the next audio clip. Only the first clip which can be played on
	 * the client will be used.
	 *
	 * @param audio multiple formats for the same the audio clip
	 */
	public void setAudio(final Audio[] audio) {
		List<Audio> list = audio == null ? null : Arrays.asList(audio);
		getOrCreateComponentModel().audio = list;
	}

	/**
	 * Retrieves the audio clips associated with this WAudio.
	 *
	 * @return the audio clips, may be null
	 */
	public Audio[] getAudio() {
		List<Audio> list = getComponentModel().audio;
		return list == null ? null : list.toArray(new Audio[]{});
	}

	/**
	 * Indicates whether the audio component is disabled.
	 *
	 * @return true if the component is disabled, otherwise false
	 */
	@Override
	public boolean isDisabled() {
		return isFlagSet(ComponentModel.DISABLED_FLAG);
	}

	/**
	 * Sets whether the audio component is disabled.
	 *
	 * @param disabled if true, the component is disabled. If false, it is enabled
	 */
	@Override
	public void setDisabled(final boolean disabled) {
		setFlag(ComponentModel.DISABLED_FLAG, disabled);
	}

	/**
	 * @return true if the clip should start playing automatically, false for a manual start
	 */
	public boolean isAutoplay() {
		return getComponentModel().autoplay;
	}

	/**
	 * <p>
	 * Sets whether the clip should play automatically. It is <strong>recommended</strong> that this should not be set
	 * true.
	 * </p>
	 * <p>
	 * Each instance of WAudio which is set to auto-play must comply with
	 * <a href="https://www.w3.org/WAI/WCAG20/quickref/#visual-audio-contrast-dis-audio">guideline 1.4.2</a>; therefore
	 * this setting is ignored if the WAudio component uses {@link Controls#NONE}.
	 *</p>
	 *
	 * @param autoplay true to start playing automatically, false for a manual start
	 */
	public void setAutoplay(final boolean autoplay) {
		getOrCreateComponentModel().autoplay = autoplay;
	}

	/**
	 * @return the media group name.
	 */
	public String getMediaGroup() {
		return getComponentModel().mediaGroup;
	}

	/**
	 * Sets the media group. Not currently implemented in the client due to lack of browser support.
	 *
	 * @param mediaGroup The media group name
	 */
	public void setMediaGroup(final String mediaGroup) {
		getOrCreateComponentModel().mediaGroup = mediaGroup;
	}

	/**
	 * Indicates whether the audio clip playback should loop.
	 *
	 * @return true to loop, false to stop at the end
	 */
	public boolean isLoop() {
		return getComponentModel().loop;
	}

	/**
	 * Sets whether the audio clip playback should loop or stop at the end. It is <strong>recommended</strong>
	 * that this not be set <code>true</code> as this could cause significant usability issues for some users.
	 *
	 * @param loop true to loop, false to stop at the end
	 */
	public void setLoop(final boolean loop) {
		getOrCreateComponentModel().loop = loop;
	}

	/**
	 * Indicates how pre-loading of content should occur before the clip is played.
	 *
	 * @return the pre-loading mode
	 */
	public Preload getPreload() {
		return getComponentModel().preload;
	}

	/**
	 * Sets how pre-loading of content should occur before the clip is played.
	 *
	 * @param preload the pre-loading mode
	 */
	public void setPreload(final Preload preload) {
		getOrCreateComponentModel().preload = preload;
	}

	/**
	 * @return alternative text to display when the audio clip can not be played.
	 */
	public String getAltText() {
		return getComponentModel().altText;
	}

	/**
	 * Sets the alternative text to display when the audio clip can not be played.
	 *
	 * @param altText the text to set
	 */
	public void setAltText(final String altText) {
		getOrCreateComponentModel().altText = altText;
	}

	/**
	 * Creates dynamic URLs that the audio clips can be loaded from. In fact the URL points to the main application
	 * servlet, but includes a non-null for the parameter associated with this WComponent (ie, its label). The
	 * handleRequest method below detects this when the browser requests a file.
	 *
	 * @return the urls to load the audio files from, or null if there are no clips defined
	 */
	public String[] getAudioUrls() {
		Audio[] audio = getAudio();

		if (audio == null || audio.length == 0) {
			return null;
		}

		String[] urls = new String[audio.length];

		Environment env = getEnvironment();
		Map<String, String> parameters = env.getHiddenParameters();
		parameters.put(Environment.TARGET_ID, getTargetId());

		if (Util.empty(getCacheKey())) {
			// Add some randomness to the URL to prevent caching
			String random = WebUtilities.generateRandom();
			parameters.put(Environment.UNIQUE_RANDOM_PARAM, random);
		} else {
			// Remove step counter as not required for cached content
			parameters.remove(Environment.STEP_VARIABLE);
			parameters.remove(Environment.SESSION_TOKEN_VARIABLE);
			// Add the cache key
			parameters.put(Environment.CONTENT_CACHE_KEY, getCacheKey());
		}

		// this variable needs to be set in the portlet environment.
		String url = env.getWServletPath();

		for (int i = 0; i < urls.length; i++) {
			parameters.put(AUDIO_INDEX_REQUEST_PARAM_KEY, String.valueOf(i));
			urls[i] = WebUtilities.getPath(url, parameters, true);
		}

		return urls;
	}

	/**
	 * Override isVisible to also return false if there are no audio clips to play.
	 *
	 * @return true if this component is visible in the given context, otherwise false
	 */
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) {
			return false;
		}

		Audio[] audio = getAudio();
		return audio != null && audio.length > 0;
	}

	/**
	 * When an audio element is rendered to the client, the browser will make a second request to get the audio content.
	 * The handleRequest method has been overridden to detect whether the request is the "content fetch" request by
	 * looking for the parameter that we encode in the content url.
	 *
	 * @param request the request being responded to
	 */
	@Override
	public void handleRequest(final Request request) {
		super.handleRequest(request);

		if (isDisabled()) {
			return;
		}

		String targ = request.getParameter(Environment.TARGET_ID);
		String audioFileRequested = request.getParameter(AUDIO_INDEX_REQUEST_PARAM_KEY);
		boolean contentReqested = targ != null && targ.equals(getTargetId());

		if (contentReqested) {
			int audioFileIndex = 0;

			try {
				audioFileIndex = Integer.parseInt(audioFileRequested);
			} catch (NumberFormatException e) {
				LOG.error("Failed to parse audio index: " + audioFileIndex);
			}

			Audio[] audio = getAudio();

			if (audio != null && audioFileIndex >= 0 && audioFileIndex < audio.length) {
				ContentEscape escape = new ContentEscape(audio[audioFileIndex]);
				escape.setCacheable(!Util.empty(getCacheKey()));
				throw escape;
			} else {
				LOG.error("Requested invalid audio clip: " + audioFileIndex);
			}
		}
	}

	/**
	 * @return the cacheKey
	 */
	public String getCacheKey() {
		return getComponentModel().cacheKey;
	}

	/**
	 * Set a cache key to make the audio cacheable on the client. All audio which it is permissible to cache should have
	 * a cache key set. Audio* which is never to be reproduced (such as an audio CAPTCHA) should not have a cache key
	 * set.
	 *
	 * @param cacheKey the cacheKey to set
	 */
	public void setCacheKey(final String cacheKey) {
		getOrCreateComponentModel().cacheKey = cacheKey;
	}

	/**
	 * Indicates which playback controls to display on the audio component.
	 *
	 * @return the playback controls to display
	 */
	public Controls getControls() {
		return getComponentModel().controls;
	}

	/**
	 * Sets which playback controls to display on the audio component. Passing a null or empty set of controls will
	 * cause the client's default set of controls to be used.
	 *
	 * @param controls the playback controls to display
	 */
	public void setControls(final Controls controls) {
		getOrCreateComponentModel().controls = controls;
	}

	/**
	 * Returns the id to use to target this component.
	 *
	 * @return this component's target id
	 */
	@Override
	public String getTargetId() {
		return getId();
	}

	/**
	 * @return a String representation of this component usually for debugging purposes
	 */
	@Override
	public String toString() {
		String text = getAltText();
		return toString(text == null ? null : '"' + text + '"');
	}

	// --------------------------------
	// Extrinsic state management
	/**
	 * Creates a new component model appropriate for this component.
	 *
	 * @return a new AudioModel
	 */
	@Override
	protected AudioModel newComponentModel() {
		return new AudioModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // For type safety only
	protected AudioModel getComponentModel() {
		return (AudioModel) super.getComponentModel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // For type safety only
	protected AudioModel getOrCreateComponentModel() {
		return (AudioModel) super.getOrCreateComponentModel();
	}

	/**
	 * Holds the extrinsic state information of a WAudio.
	 */
	public static class AudioModel extends ComponentModel {

		/**
		 * The various audio clips.
		 */
		private List<Audio> audio;

		/**
		 * The cache key used to control client-side caching.
		 */
		private String cacheKey;

		/**
		 * Indicates whether the audio should play immediately after page load.
		 */
		private boolean autoplay;

		/**
		 * Indicates whether playback of the audio clip should be looped.
		 */
		private boolean loop;

		/**
		 * Indicates which playback controls to display.
		 */
		private Controls controls;

		/**
		 * Indicates whether pre-loading of content should occur before the clip is played.
		 */
		private Preload preload = Preload.NONE;

		/**
		 * Alternate text to display if the audio clip can not be played.
		 */
		private String altText;

		/**
		 * This is used to group related media together, for example to synchronize tracks.
		 */
		private String mediaGroup;
	}
}
