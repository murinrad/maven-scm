package org.apache.maven.scm.provider;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.NoSuchCommandScmException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.log.ScmLogDispatcher;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;
import org.apache.maven.scm.command.Command;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class AbstractScmProvider
    implements ScmProvider
{
    private boolean isInitialized;

    private ScmLogDispatcher logDispatcher = new ScmLogDispatcher();

    private Map cmds;

    protected abstract Map getCommands();

    // ----------------------------------------------------------------------
    // Component Lifecycle
    // ----------------------------------------------------------------------

    public final void initialize()
    {
        cmds = getCommands();

        if ( cmds == null )
        {
            cmds = Collections.EMPTY_MAP;
        }

        if ( cmds.size() == 0 )
        {
            logDispatcher.warn( "No SCM commands defined for SCM type " + getScmType() );
        }

        if ( logDispatcher.isDebugEnabled() )
        {
            logDispatcher.debug( "Registered " + getScmType() + " SCM:" );

            for ( Iterator it = cmds.keySet().iterator(); it.hasNext(); )
            {
                String name = (String) it.next();

                logDispatcher.debug( "  " + name );
            }
        }

        isInitialized = true;
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    public List validateScmUrl( String scmSpecificUrl, char delimiter )
    {
        List messages = new ArrayList();

        try
        {
            makeProviderScmRepository( scmSpecificUrl, delimiter );
        }
        catch ( ScmRepositoryException e )
        {
            messages.add( e.getMessage() );
        }

        return messages;
    }

    // ----------------------------------------------------------------------
    // Scm Implementation
    // ----------------------------------------------------------------------

    public ScmResult execute( String commandName, ScmProviderRepository repository, ScmFileSet fileSet,
                              CommandParameters parameters )
        throws ScmException
    {
        Command command = getCommand( commandName );

        return command.execute( repository, fileSet, parameters );
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    protected Command getCommand( String name )
        throws ScmException
    {
        if ( !isInitialized )
        {
            initialize();
        }

        Command command = (Command) cmds.get( name );

        command.setLogger( logDispatcher );

        if ( command == null )
        {
            throw new NoSuchCommandScmException( name );
        }

        return command;
    }

    /**
     * @see org.apache.maven.scm.provider.ScmProvider#addListener(org.apache.maven.scm.log.ScmLogger)
     */
    public void addListener( ScmLogger logger )
    {
        logDispatcher.addListener( logger );
    }

    public ScmLogger getLogger()
    {
        return logDispatcher;
    }

    /**
     * @see org.apache.maven.scm.provider.ScmProvider#makeProviderScmRepository(java.io.File, java.lang.String)
     */
    public ScmProviderRepository makeProviderScmRepository( File path )
        throws ScmRepositoryException, UnknownRepositoryStructure
    {
        throw new UnknownRepositoryStructure();
    }
}
