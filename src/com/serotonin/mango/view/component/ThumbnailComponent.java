/*
    Mango - Open Source M2M - http://mango.serotoninsoftware.com
    Copyright (C) 2006-2011 Serotonin Software Technologies Inc.
    @author Matthew Lohbihler
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Objects;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.ImageValue;
import com.serotonin.mango.view.ImplDefinition;

/**
 * @author Matthew Lohbihler
 */
@JsonRemoteEntity
public class ThumbnailComponent extends PointComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("thumbnailImage", "THUMBNAIL",
            "graphic.thumbnailImage", new int[] { DataTypes.IMAGE });

    @JsonRemoteProperty
    private int scalePercent;

    public ThumbnailComponent() {}

    public ThumbnailComponent(ThumbnailComponent thumbnailComponent) {
        super(thumbnailComponent);
        this.scalePercent = thumbnailComponent.getScalePercent();
    }

    public int getScalePercent() {
        return scalePercent;
    }

    public void setScalePercent(int scalePercent) {
        this.scalePercent = scalePercent;
    }

    @Override
    public ViewComponent copy() {
        return new ThumbnailComponent(this);
    }

    @Override
    public String snippetName() {
        return "imageValueContent";
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public void addDataToModel(Map<String, Object> model, PointValueTime pointValue) {
        if (pointValue == null || pointValue.getValue() == null) {
            model.put("error", "common.noData");
            return;
        }

        if (!(pointValue.getValue() instanceof ImageValue)) {
            model.put("error", "common.thumb.invalidValue");
            return;
        }

        ImageValue imageValue = (ImageValue) pointValue.getValue();
        model.put("imageType", imageValue.getTypeExtension());
        model.put("scalePercent", scalePercent);
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);

        out.writeInt(scalePercent);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1)
            scalePercent = in.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThumbnailComponent)) return false;
        if (!super.equals(o)) return false;
        ThumbnailComponent that = (ThumbnailComponent) o;
        return getScalePercent() == that.getScalePercent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getScalePercent());
    }

    @Override
    public String toString() {
        return "ThumbnailComponent{" +
                "scalePercent=" + scalePercent +
                "} " + super.toString();
    }
}
