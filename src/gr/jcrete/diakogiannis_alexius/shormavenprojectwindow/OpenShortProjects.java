/**
    *This file is part of Foobar.
    *
    *Foobar is free software: you can redistribute it and/or modify
    *it under the terms of the GNU General Public License as published by
    *the Free Software Foundation, either version 3 of the License, or
    *(at your option) any later version.
    *
    *Foobar is distributed in the hope that it will be useful,
    *but WITHOUT ANY WARRANTY; without even the implied warranty of
    *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    *GNU General Public License for more details.
    *
    *You should have received a copy of the GNU General Public License
    *along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
    * 
    * @author Diakogiannis Alexius
    * @author Jaroslav Tulach
 */
package gr.jcrete.diakogiannis_alexius.shormavenprojectwindow;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
    category = "Window",
    id = "gr.jcrete.diakogiannis_alexius.shormavenprojectwindow.OpenShortProjects"
)
@ActionRegistration(
    displayName = "#CTL_OpenShortProjects"
)
@ActionReference(path = "Menu/Window", position = 0)
@Messages("CTL_OpenShortProjects=Open Short Maven Project Window")
public final class OpenShortProjects implements ActionListener {

    private final Node context;

    public OpenShortProjects(Node context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        class Explorer extends TopComponent implements ExplorerManager.Provider {
            ExplorerManager em = new ExplorerManager();

            @Override
            public ExplorerManager getExplorerManager() {
                return em;
            }
        }
        Explorer e = new Explorer();
        e.getExplorerManager().setRootContext(new ShorterNameNode(context));
        e.setLayout(new BorderLayout());
        e.add(new BeanTreeView(), BorderLayout.CENTER);

        e.setDisplayName("Short Project View Of " + context.getDisplayName());
        e.open();
        e.requestActive();
    }

    private static class ShorterNameNode extends FilterNode {

        public ShorterNameNode(Node n) {
            super(n, new ShorterNameChildren(n));
        }

        @Override
        public String getDisplayName() {
            Project p = getOriginal().getLookup().lookup(Project.class);
            if (p != null && p.getClass().getName().contains("NbMavenProject")) {
                final FileObject dir = p.getProjectDirectory();
                return dir.getName();
            }

            return super.getDisplayName();
        }


    }

    private static class ShorterNameChildren extends FilterNode.Children {
        public ShorterNameChildren(Node or) {
            super(or);
        }

        @Override
        protected Node[] createNodes(Node key) {
            return new Node[] { new ShorterNameNode(key) };
        }

    }
}
