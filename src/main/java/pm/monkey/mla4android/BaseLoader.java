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
import java.io.InputStream;

import pm.monkey.mla4j.Container;
import pm.monkey.mla4j.FormatException;
import pm.monkey.mla4j.Loader;
import android.content.res.AssetFileDescriptor;

public abstract class BaseLoader {

    protected final Loader loader;

    protected BaseLoader() {
        loader = new Loader();
    }

    protected Container loadContainer(InputStream is) throws FormatException, IOException {
        return loader.load(is);
    }

    protected Container loadContainer(AssetFileDescriptor fd) throws FormatException, IOException {
        InputStream is = fd.createInputStream();

        return loadContainer(is);
    }
}
