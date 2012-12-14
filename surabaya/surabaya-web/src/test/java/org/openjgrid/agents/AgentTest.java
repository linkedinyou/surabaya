/**
 *  Surabaya - a replacement http server for the OpenSimulator
 *  Copyright (C) 2012 Akira Sonoda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openjgrid.agents;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Akira Sonoda
 *
 */
public class AgentTest {
	
	private Agent anAgent = null;
	private String jsonstring = "{\"agent_id\":\"3dcad562-c070-4d58-b735-2f04f790a76c\",\"caps_path\":\"a8ff164b-a020-4280-bbaf-457176d68c4c\",\"circuit_code\":\"392918621\",\"first_name\":\"Akira\",\"last_name\":\"Sonoda\",\"secure_session_id\":\"70d290e1-926c-4bd8-89ce-18c3e2678b50\",\"session_id\":\"b4d760a3-6460-45b4-9fa7-0ef9d647cb24\",\"start_pos\":\"<128, 128, 0>\",\"client_ip\":\"127.0.0.1\",\"viewer\":\"Cool VL Viewer 1.26.4.20\",\"channel\":\"Cool VL Viewer\",\"mac\":\"5bde1b49882e61c5a4c65dae702eca8c\",\"id0\":\"3f7dec67d744b3bd103cef18d5975ea6\",\"packed_appearance\":{\"height\":1.63676404953003,\"wearables\":[[{\"item\":\"c6f1ee03-d19a-4dec-aaea-163d434ac07c\",\"asset\":\"62604416-e0a6-5786-be70-fa5cafe61e10\"}],[{\"item\":\"ea78827a-69e4-4bb5-9d68-4bb267ad9498\",\"asset\":\"ee19bb0a-29cc-c4cd-b55a-21ec9719f495\"}],[{\"item\":\"6b300e31-9900-4971-91b6-af37704f2bd8\",\"asset\":\"e91f4520-82ba-deb2-f618-6c6a9ed9c86f\"}],[{\"item\":\"606933ed-cb82-48ce-8421-d4168dba2250\",\"asset\":\"e3375c0b-4eee-4ecd-bde5-bc10e479e713\"}],[],[{\"item\":\"6d727cc8-cce4-4b1f-9033-8f18671ba058\",\"asset\":\"8b49f7df-3eeb-4b6f-87a0-0874c43c9409\"}],[{\"item\":\"414f2cf9-d95e-4798-996a-1c672a6f93b7\",\"asset\":\"b1b9cc9a-f5d1-4910-90e4-3c987bad73ce\"}],[],[{\"item\":\"e7d88384-22bc-4486-9e1c-9e217cb6fd01\",\"asset\":\"e9b2aa9b-0512-2e28-7d72-6414511adba4\"}],[],[{\"item\":\"ce87ddeb-10d2-4492-8e2a-14608acd709b\",\"asset\":\"64c5d353-6ce3-48c1-ac47-a9e42f695da2\"}],[],[],[],[]],\"textures\":[\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\",\"c228d1cf-4b5d-4ba8-84f4-899a0796aa97\"],\"visualparams\":[25,27,95,178,52,20,22,81,0,28,178,164,102,85,129,86,163,81,0,127,0,122,56,91,33,68,0,68,119,122,142,0,203,255,255,48,0,0,127,0,0,127,0,0,0,127,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0,0,0,0,0,25,0,0,0,0,0,0,0,15,0,145,104,0,0,160,76,69,85,127,127,5,85,42,100,216,214,204,204,204,51,25,89,76,204,0,178,0,0,172,74,86,66,102,30,0,127,186,127,127,127,130,0,72,48,127,127,110,38,79,102,181,145,63,0,0,0,0,127,127,0,0,0,0,127,0,159,0,0,255,0,89,0,0,135,186,107,98,163,0,0,188,27,63,135,0,214,204,198,0,0,68,43,124,226,255,198,255,255,255,255,255,255,255,255,255,204,0,255,255,255,255,255,255,255,255,255,255,255,0,255,255,255,255,255,0,127,142,255,25,100,255,255,255,255,84,0,0,0,51,0,255,255,255],\"attachments\":[{\"point\":1,\"item\":\"64889d18-9e79-46d2-962c-83fa89dae2df\"},{\"point\":12,\"item\":\"42bd1e24-bb3f-4d9b-9820-d1033871498a\"},{\"point\":14,\"item\":\"e2d1ea37-9085-4271-ac47-d355f3c5ae23\"},{\"point\":17,\"item\":\"ca112a2b-a0ba-4d0a-bb0c-05c37a2c9c21\"},{\"point\":18,\"item\":\"bcb99c2e-72fa-4d5e-8803-41abf68595df\"},{\"point\":2,\"item\":\"afc9c18d-710f-49e9-b0fe-fe9724992a24\"},{\"point\":20,\"item\":\"a5d22082-0a82-48c9-bc36-6f28c39c3484\"},{\"point\":23,\"item\":\"82c0150f-b0a9-41b6-ad6f-209dd63aa701\"},{\"point\":24,\"item\":\"42293c89-43cd-4ed5-8174-67f0b28198b5\"},{\"point\":26,\"item\":\"bdc37207-2786-401d-b820-4b8f51b946f1\"},{\"point\":27,\"item\":\"682098b4-1749-4c85-a11c-08d6a49a73d5\"},{\"point\":28,\"item\":\"4d77cf18-0f22-488c-88e9-82680c534d20\"},{\"point\":31,\"item\":\"b06f1451-a5be-4d63-a406-b8616c57d9b9\"},{\"point\":7,\"item\":\"96dd1512-1e10-4377-8f86-0085c9e3028f\"},{\"point\":8,\"item\":\"84406ee2-b0c6-43a0-be27-b3e76bfdfbeb\"},{\"point\":9,\"item\":\"aa8d29de-0fda-43cf-be47-ac6f0d4d65e4\"}]},\"service_urls\":[\"HomeURI\",\"/\",\"GatekeeperURI\",\"/\",\"InventoryServerURI\",\"/\",\"AssetServerURI\",\"/\"],\"serviceurls\":{\"HomeURI\":\"/\",\"GatekeeperURI\":\"/\",\"InventoryServerURI\":\"/\",\"AssetServerURI\":\"/\"},\"destination_x\":\"256000\",\"destination_y\":\"256000\",\"destination_name\":\"Ko Suai\",\"destination_uuid\":\"3b25bded-f609-4e7c-b25d-8affdb2c91f3\",\"teleport_flags\":\"4224\"}";

	@Before
	public void setUp() throws Exception {
		anAgent = new Agent(jsonstring);		
	}
	
	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getAgent_id()}.
	 */
	@Test
	public void testGetAgent_id() {
		UUID result = anAgent.getAgent_id();
		assert (result.equals(UUID.fromString("3dcad562-c070-4d58-b735-2f04f790a76c")));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getCaps_path()}.
	 */
	@Test
	public void testGetCaps_path() {
		String result = anAgent.getCaps_path();
		assert (result.equalsIgnoreCase("a8ff164b-a020-4280-bbaf-457176d68c4c"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getCircuit_code()}.
	 */
	@Test
	public void testGetCircuit_code() {
		String result = anAgent.getCircuit_code();
		assert (result.equalsIgnoreCase("392918621"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getFirst_name()}.
	 */
	@Test
	public void testGetFirst_name() {
		String result = anAgent.getFirst_name();
		assert (result.equalsIgnoreCase("Akira"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getLast_name()}.
	 */
	@Test
	public void testGetLast_name() {
		String result = anAgent.getLast_name();
		assert (result.equalsIgnoreCase("Sonoda"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getSecure_session_id()}.
	 */
	@Test
	public void testGetSecure_session_id() {
		UUID result = anAgent.getSecure_session_id();
		assert (result.equals(UUID.fromString("70d290e1-926c-4bd8-89ce-18c3e2678b50")));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getSession_id()}.
	 */
	@Test
	public void testGetSession_id() {
		UUID result = anAgent.getSession_id();
		assert (result.equals(UUID.fromString("b4d760a3-6460-45b4-9fa7-0ef9d647cb24")));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getStart_pos()}.
	 */
	@Test
	public void testGetStart_pos() {
		String result = anAgent.getStart_pos();
		assert (result.equalsIgnoreCase("<128, 128, 0>"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getClient_ip()}.
	 */
	@Test
	public void testGetClient_ip() {
		String result = anAgent.getClient_ip();
		assert (result.equalsIgnoreCase("127.0.0.1"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getViewer()}.
	 */
	@Test
	public void testGetViewer() {
		String result = anAgent.getViewer();
		assert (result.equalsIgnoreCase("Cool VL Viewer 1.26.4.20"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getChannel()}.
	 */
	@Test
	public void testGetChannel() {
		String result = anAgent.getChannel();
		assert (result.equalsIgnoreCase("Cool VL Viewer"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getMac()}.
	 */
	@Test
	public void testGetMac() {
		String result = anAgent.getMac();
		assert (result.equalsIgnoreCase("5bde1b49882e61c5a4c65dae702eca8c"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getId0()}.
	 */
	@Test
	public void testGetId0() {
		String result = anAgent.getId0();
		assert (result.equalsIgnoreCase("3f7dec67d744b3bd103cef18d5975ea6"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getService_urls()}.
	 */
	@Test
	public void testGetService_urls() {
		Map<String, String> result = anAgent.getService_urls();
		assert (result.get("HomeURI").equals("/"));
		assert (result.get("GatekeeperURI").equals("/"));
		assert (result.get("InventoryServerURI").equals("/"));
		assert (result.get("AssetServerURI").equals("/"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getDestination_x()}.
	 */
	@Test
	public void testGetDestination_x() {
		String result = anAgent.getDestination_x();
		assert (result.equalsIgnoreCase("256000"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getDestination_y()}.
	 */
	@Test
	public void testGetDestination_y() {
		String result = anAgent.getDestination_y();
		assert (result.equalsIgnoreCase("256000"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getDestination_name()}.
	 */
	@Test
	public void testGetDestination_name() {
		String result = anAgent.getDestination_name();
		assert (result.equalsIgnoreCase("Ko Suai"));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getDestination_uuid()}.
	 */
	@Test
	public void testGetDestination_uuid() {
		UUID result = anAgent.getDestination_uuid();
		assert (result.equals(UUID.fromString("3b25bded-f609-4e7c-b25d-8affdb2c91f3")));
	}

	/**
	 * Test method for {@link org.openjgrid.agents.Agent#getTeleport_flags()}.
	 */
	@Test
	public void testGetTeleport_flags() {
		String result = anAgent.getTeleport_flags();
		assert (result.equalsIgnoreCase("4224"));
	}

}
