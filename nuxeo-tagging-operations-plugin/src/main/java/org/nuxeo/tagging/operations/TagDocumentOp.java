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
 * @since 5.9.6
 */
@Operation(id = TagDocumentOp.ID, category = Constants.CAT_DOCUMENT, label = "Tag Document", description = "Add the tags <code>labels</code> (comma-separated list) to the current document. The <code>TagService</code> cleans each label (lowercase, remove spaces and punctuation, ...) before adding them.")
public class TagDocumentOp {

    public static final String ID = "Document.Tag";

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
                    "Cannot add the tag(s) because the TagService is not available");
        }

        if (labels != null && labels.length() > 0) {

            // When cleaning up the label, the TagService will also remove the
            // spaces, so it's ok if the labels are separated with ", " instead
            // of just ","
            String[] arrayLabels = labels.split(",");
            for (String theLabel : arrayLabels) {
                String userName = TaggingUtilsHelper.getOriginatingUserOrCurrentUser(session);
                tagService.tag(session, inDoc.getId(), theLabel, userName);
            }
        }

        return inDoc;
    }
}
