/**
 * Copyright (c) 2013 Monkey By Monkey
 * 
 * This file is part of mla4android.
 * mla4android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * mla4android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with mla4android. If not, see <http://www.gnu.org/licenses/>.
 */

package pm.monkey.mla4android;

import java.io.IOException;

import pm.monkey.mla4j.Container;
import pm.monkey.mla4j.FormatException;
import pm.monkey.mla4j.Header;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class MediaPlayerLoader extends BaseLoader {

    public Players loadPlayers(AssetFileDescriptor assetFd) throws FormatException, IOException {
        Container container = loadContainer(assetFd);

        Header header = container.getHeader();

        Players players = new Players();

        MediaPlayer loopAreaPlayer = new MediaPlayer();

        loopAreaPlayer.setDataSource(assetFd.getFileDescriptor(),
                assetFd.getStartOffset() + header.getSize() + header.getLoopAreaOffset(),
                header.getLoopAreaSize());
        loopAreaPlayer.prepare();

        loopAreaPlayer.setLooping(true);

        players.loopAreaPlayer = loopAreaPlayer;

        if (header.getLoopAreaOffset() > 0) {
            MediaPlayer introPlayer = new MediaPlayer();
            introPlayer.setDataSource(assetFd.getFileDescriptor(), assetFd.getStartOffset() + header.getSize(), header.getLoopAreaOffset());
            introPlayer.prepare();

            introPlayer.setLooping(false);
            introPlayer.setNextMediaPlayer(loopAreaPlayer);

            players.introPlayer = introPlayer;
        }

        long fileSize = assetFd.getLength();
        long outroOffset = assetFd.getStartOffset() + header.getSize() + header.getLoopAreaOffset() + header.getLoopAreaSize();

        if (outroOffset < fileSize) {
            MediaPlayer outroPlayer = new MediaPlayer();
            outroPlayer.setDataSource(assetFd.getFileDescriptor(), outroOffset, fileSize - outroOffset);
            outroPlayer.prepare();

            outroPlayer.setLooping(false);
            loopAreaPlayer.setNextMediaPlayer(outroPlayer);

            players.outroPlayer = outroPlayer;
        }

        assetFd.close();

        return players;
    }

    public Players loadPlayers(Context context, int resourceId) throws FormatException, IOException {
        AssetFileDescriptor assetFd = context.getResources().openRawResourceFd(resourceId);

        return loadPlayers(assetFd);
    }

    public MediaPlayer load(Context context, int resourceId) throws FormatException, IOException {

        Players players = loadPlayers(context, resourceId);

        if (players.getIntroPlayer() != null) {
            return players.getIntroPlayer();
        } else {
            return players.getLoopAreaPlayer();
        }
    }

    public MediaPlayer load(AssetFileDescriptor assetFd) throws FormatException, IOException {
        Players players = loadPlayers(assetFd);

        if (players.getIntroPlayer() != null) {
            return players.getIntroPlayer();
        } else {
            return players.getLoopAreaPlayer();
        }
    }

    public static class Players {

        private MediaPlayer introPlayer;

        private MediaPlayer loopAreaPlayer;

        private MediaPlayer outroPlayer;

        public MediaPlayer getIntroPlayer() {
            return introPlayer;
        }

        public MediaPlayer getLoopAreaPlayer() {
            return loopAreaPlayer;
        }

        public MediaPlayer getOutroPlayer() {
            return outroPlayer;
        }

    }
}
