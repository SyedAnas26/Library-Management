package util;

import java.sql.ResultSet;

@FunctionalInterface
public interface DBQueryRunner
{
	Object execute(ResultSet r) throws Exception;
}