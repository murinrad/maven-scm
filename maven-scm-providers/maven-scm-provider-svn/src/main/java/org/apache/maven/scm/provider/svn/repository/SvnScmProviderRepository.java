package org.apache.maven.scm.provider.svn.repository;

/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.scm.provider.ScmProviderRepository;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public class SvnScmProviderRepository extends ScmProviderRepository
{
    /** */
    private final String url;

    /** */
    private final String user;

    /** */
    private final String password;

    /**
     * The base directory for any tags, relative to the URL given. Default is <code>../tags</code>.
     */
    private String tagBase;

    public SvnScmProviderRepository( String url, String user, String password )
    {
        this.url = url;

        this.tagBase = url.substring( 0, url.lastIndexOf( '/' ) ) + "/tags";

        this.user = user;

        this.password = password;
    }

    public String getUrl()
    {
        return url;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUser()
    {
        return user;
    }

    public String getModule()
    {
        return deriveModuleFromUrl( url );
    }

    public String getTagBase()
    {
        return tagBase;
    }

    public void setTagBase( String tagBase )
    {
        this.tagBase = tagBase;
    }
}