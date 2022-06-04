package pl.skidam.automodpack.Client.deletemods;

import pl.skidam.automodpack.utils.Download;
import pl.skidam.automodpack.utils.InternetConnectionCheck;

import static pl.skidam.automodpack.AutoModpackMain.*;

public class TrashMod {

    public TrashMod() {

        if (trashOut.exists()) {
            return;
        }

        InternetConnectionCheck.InternetConnectionCheck();

        LOGGER.info("Downloading TrashMod!");

        // Download and check if download is successful *magic*

        if (!Download.Download(trashLink, trashOut)) {
            LOGGER.error("Failed to download TrashMod!");
            return;
        }
        LOGGER.info("Successfully downloaded TrashMod!");
    }
}