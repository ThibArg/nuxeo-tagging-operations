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

package org.nuxeo.tagging.operations.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.test.EmbeddedAutomationServerFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.tag.TagService;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.tagging.operations.*;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class, CoreFeature.class,
        EmbeddedAutomationServerFeature.class })
@Deploy({ "org.nuxeo.ecm.platform.tag", "nuxeo-tagging-operations" })
public class TesTaggingOperations {

    private static final String TAG_1 = "one";
    private static final String TAG_2 = "AnD Two";
    private static final String TAG_3 = "Three";
    private static final String TAGS = TAG_1 + "," + TAG_2 + ", " + TAG_3;
    private static final String TAG_1_formatted = "one";
    private static final String TAG_2_formatted = "andtwo";
    private static final String TAG_3_formatted = "three";

    protected DocumentModel theFileDoc;
    protected String theFileDocId;

    @Inject
    CoreSession coreSession;

    @Inject
    AutomationService automationService;

    @Inject
    TagService tagService;

    @Before
    public void setUp() {
        assertNotNull(coreSession);
        assertNotNull(automationService);
        assertNotNull(tagService);

        theFileDoc = coreSession.createDocumentModel("/", "File", "File");
        assertNotNull(theFileDoc);
        theFileDoc = coreSession.createDocument(theFileDoc);
        assertNotNull(theFileDoc);
        theFileDocId = theFileDoc.getId();
    }

    @Test
    public void testTagDocument() throws Exception {
        OperationContext ctx = new OperationContext(coreSession);
        assertNotNull(ctx);

        ctx.setInput(theFileDoc);
        OperationChain chain = new OperationChain("testChain");
        chain.add(TagDocumentOp.ID).set("labels",  TAGS);
        automationService.run(ctx, chain);

        checkTag(TAG_1_formatted);
        checkTag(TAG_2_formatted);
        checkTag(TAG_3_formatted);

    }

    protected void checkTag(String inTag) {
        List<String> docIds = tagService.getTagDocumentIds(coreSession, inTag, null);
        assertNotNull(docIds);
        assertEquals(1, docIds.size());
        assertEquals(theFileDocId, docIds.get(0));
    }

    @Test
    public void testUntagDocument() throws Exception {

        OperationContext ctx = new OperationContext(coreSession);
        assertNotNull(ctx);ctx.setInput(theFileDoc);

        OperationChain chain = new OperationChain("testChain");
        chain.add(UntagDocumentOp.ID).set("labels",  TAG_1);
        automationService.run(ctx, chain);

        List<String> docIds = tagService.getTagDocumentIds(coreSession, TAG_1, null);
        assertEquals(0, docIds.size());
    }

    @Test
    public void testRemoveTags() throws Exception {

        OperationContext ctx = new OperationContext(coreSession);
        assertNotNull(ctx);ctx.setInput(theFileDoc);

        OperationChain chain = new OperationChain("testChain");
        chain.add(RemoveTagsFromDocumentOp.ID);
        automationService.run(ctx, chain);

        assertEquals(0, tagService.getDocumentTags(coreSession, theFileDocId, null).size() );
    }

}
