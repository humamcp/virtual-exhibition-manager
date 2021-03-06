package ch.unibas.dmi.dbis.vrem.database.codec;

import ch.unibas.dmi.dbis.vrem.model.Vector3f;
import ch.unibas.dmi.dbis.vrem.model.exhibition.Exhibit;
import ch.unibas.dmi.dbis.vrem.model.objects.CulturalHeritageObject;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

public class ExhibitCodec implements Codec<Exhibit> {

    public final String FIELD_NAME_NAME = "name";
    private final String FIELD_NAME_DESCRIPTION = "description";
    private final String FIELD_NAME_TYPE = "type";
    private final String FIELD_NAME_PATH = "path";
    private final String FIELD_NAME_POSITION = "position";
    private final String FIELD_NAME_SIZE = "size";
    private final String FIELD_NAME_AUDIO = "audio";
    private final String FIELD_NAME_LIGHT = "light";


    private final Codec<Vector3f> codec;

    /**
     *
     */
    public ExhibitCodec(CodecRegistry registry) {
        this.codec = registry.get(Vector3f.class);
    }

    @Override
    public Exhibit decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        ObjectId id = null;
        String name = null;
        String description = null;
        CulturalHeritageObject.CHOType type = null;
        String path = null;
        Vector3f position = null;
        Vector3f size = null;
        String guide = null;
        boolean light = false;

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            switch (reader.readName()) {
                case FIELD_NAME_NAME:
                    name = reader.readString();
                    break;
                case FIELD_NAME_DESCRIPTION:
                    description = reader.readString();
                    break;
                case FIELD_NAME_TYPE:
                    type = CulturalHeritageObject.CHOType.valueOf(reader.readString());
                    break;
                case FIELD_NAME_PATH:
                    path = reader.readString();
                    break;
                case FIELD_NAME_POSITION:
                    position = this.codec.decode(reader, decoderContext);
                    break;
                case FIELD_NAME_SIZE:
                    size = this.codec.decode(reader, decoderContext);
                    break;
                case FIELD_NAME_AUDIO:
                    guide = reader.readString();
                    break;
                case FIELD_NAME_LIGHT:
                    light = reader.readBoolean();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.readEndDocument();
        if (id == null) {
            id = new ObjectId();
        }
        return new Exhibit(id, name, description, path, type, position, size, guide, light);
    }

    @Override
    public void encode(BsonWriter writer, Exhibit value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString(FIELD_NAME_NAME, value.name);
        writer.writeString(FIELD_NAME_DESCRIPTION, value.description);
        writer.writeString(FIELD_NAME_TYPE, value.type.name());
        writer.writeString(FIELD_NAME_PATH, value.path);
        writer.writeName(FIELD_NAME_POSITION);
        this.codec.encode(writer, value.position, encoderContext);
        writer.writeName(FIELD_NAME_SIZE);
        this.codec.encode(writer, value.size, encoderContext);
        if (value.audio != null) {
            writer.writeString(FIELD_NAME_AUDIO, value.audio);
        }
        writer.writeBoolean(FIELD_NAME_LIGHT, value.light);
        writer.writeEndDocument();
    }

    @Override
    public Class<Exhibit> getEncoderClass() {
        return Exhibit.class;
    }
}
