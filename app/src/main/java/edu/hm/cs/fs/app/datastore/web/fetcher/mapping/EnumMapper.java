package edu.hm.cs.fs.app.datastore.web.fetcher.mapping;

/**
 * Created by Fabio on 19.02.2015.
 */
public class EnumMapper<T extends IKeyConstant> implements Converter<T> {
	@Override
	public void serialize(final T item, final ObjectWriter writer,
						  final Context ctx) throws Exception {
		writer.beginObject();
		// writer.writeName(item.getClass().getSimpleName());
		writer.writeString("key", item.getKey());
		writer.endObject();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(final ObjectReader reader, final Context ctx)
			throws Exception {
		T result = null;
		reader.beginObject();

		while (reader.hasNext()) {
			reader.next();
			if ("key".equals(reader.name())) {
				System.out.println(getGenericClass().getSimpleName());
				final Method method = getGenericClass().getMethod("of",
						String.class);
				result = (T) method.invoke(null, reader.valueAsString());
			}
		}

		reader.endObject();

		return result;
	}

	@SuppressWarnings("unchecked")
	private Class<T> getGenericClass() {
		final ParameterizedType genericSuperclass = (ParameterizedType) new ArrayList<T>()
				.getClass().getGenericSuperclass();
		return (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}
}
