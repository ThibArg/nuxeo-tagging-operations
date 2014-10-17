/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Thibaud Arguillere
 */
package org.nuxeo.tagging.operations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.tag.TagService;
import org.nuxeo.runtime.api.Framework;

/**
 * Utility class, just to centralize the code (this is called by
 * at least 3 operations)
 *
 * @since 5.9.6
 */
public class TaggingUtilsHelper {

    private static final Log log = LogFactory.getLog(TaggingUtilsHelper.class);

    public static TagService tagService = null;

    protected static boolean tagServiceChecked = false;

    public static TagService getActiveTagService() {
        if (!tagServiceChecked) {
            try {
                tagService = Framework.getService(TagService.class);
            } catch (Exception e) {
                tagService = null;
                log.error("Cannot get TagService.", e);
            }
            if (!tagService.isEnabled()) {
                tagService = null;
                log.error("TagService is not activated.");
            }
            tagServiceChecked = true;
        }

        return tagService;
    }

    public static String getOriginatingUserOrCurrentUser(CoreSession inSession) {
        String userName = null;
        NuxeoPrincipal nxPcipal = (NuxeoPrincipal) inSession.getPrincipal();
        if (nxPcipal != null && nxPcipal.getOriginatingUser() != null) {
            userName = nxPcipal.getOriginatingUser();
        } else {
            userName = inSession.getPrincipal().getName();
        }

        return userName;
    }

}
