package miui.statusbar.lyric;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import miui.statusbar.lyric.config.Config;
import miui.statusbar.lyric.utils.ConfigUtils;

@TargetApi(Build.VERSION_CODES.N)
public class QuickTitleService extends TileService {
    static Tile tile;

    //    @Override
//    public void onCreate() {
//        super.onCreate();
//        Context context = getBaseContext();
//        Tile tile = getQsTile();
//        Config config = ActivityUtils.getConfig(context);
//        set(tile, config);
//    }
//
    @Override
    public void onClick() {
        super.onClick();
        Config config = ConfigUtils.getConfig(getBaseContext());
        assert config != null;
        config.setLyricService(!config.getLyricService());
        set(config);
    }

    private void set(Config config) {
        tile.setIcon(Icon.createWithResource(this, R.drawable.title_icon));
        tile.setLabel(getString(R.string.QuickTitle));
        tile.setContentDescription(getString(R.string.QuickTitle));
        tile.setState(config.getLyricService() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Config config = ConfigUtils.getConfig(getBaseContext());
        tile = getQsTile();
        assert config != null;
        set(config);
    }
}
