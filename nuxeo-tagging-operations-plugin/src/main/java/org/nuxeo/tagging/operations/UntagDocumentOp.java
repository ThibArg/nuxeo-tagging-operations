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
 *     thibaud
 */

package org.nuxeo.tagging.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.tag.TagService;

/**
 *
 */
@Operation(id = UntagDocumentOp.ID, category = Constants.CAT_DOCUMENT, label = "Untag Document", description = "Remove the tags <code>labels</code> (comma-separated list) from the current document. The <code>TagService</code> cleans each label (lowercase, remove spaces and punctuation, ...) before removing them.")
public class UntagDocumentOp {

    public static final String ID = "Document.Untag";

    protected static TagService tagService = null;

    @Context
    protected CoreSession session;

    @Param(name = "labels", required = true)
    protected String labels;

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel inDoc) throws ClientException {

        tagService = TaggingUtilsHelper.getActiveTagService();
        if (tagService == null) {
            throw new ClientException(
                    "Cannot remove the tag(s) because the TagService is not available");
        }

        if (labels != null && labels.length() > 0) {

            // When doing its cleanup, the TagService will also cleanup the
            // spaces,
            // so if the labels are separated with ", " instead of just ","
            String[] arrayLabels = labels.split(",");
            for (String theLabel : arrayLabels) {
                String userName = TaggingUtilsHelper.getOriginatingUserOrCurrentUser(session);
                tagService.untag(session, inDoc.getId(), theLabel, userName);
            }
        }

        return inDoc;
    }
}
