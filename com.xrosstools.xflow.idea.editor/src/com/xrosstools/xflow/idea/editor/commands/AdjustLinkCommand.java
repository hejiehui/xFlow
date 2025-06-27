package com.xrosstools.xflow.idea.editor.commands;

import com.xrosstools.idea.gef.commands.AbstractAdjustConnectionCommand;
import com.xrosstools.idea.gef.figures.Connection;
import com.xrosstools.idea.gef.parts.AbstractConnectionEditPart;
import com.xrosstools.idea.gef.routers.CommonStyleRouter;
import com.xrosstools.idea.gef.routers.HomolateralRouter;
import com.xrosstools.xflow.idea.editor.model.Link;

public class AdjustLinkCommand extends AbstractAdjustConnectionCommand<Integer> {
    public AdjustLinkCommand(AbstractConnectionEditPart connectionEditPart) {
        super(connectionEditPart);
    }

    @Override
    public Integer getOldConstraint(AbstractConnectionEditPart connectionEditPart) {
        return ((Link)connectionEditPart.getModel()).getDistance();
    }

    @Override
    public Integer getNewConstraint(AbstractConnectionEditPart connectionEditPart) {
        CommonStyleRouter router = (CommonStyleRouter)((Connection)connectionEditPart.getFigure()).getRouter();
        return ((HomolateralRouter)router.getInternalRouter((Connection) connectionEditPart.getFigure())).getDistance();
    }

    @Override
    public void setConstraint(AbstractConnectionEditPart connectionEditPart, Integer distance) {
        ((Link)connectionEditPart.getModel()).setDistance(distance);
    }
}
