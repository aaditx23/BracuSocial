const usis = require("./usis/scraper")

exports.getSchedule = async (req, res) => {
  const { email, password } = req.body;


  if (!email || !password) {
    return res.status(400).json({ message: "Email and password are required" });
  }

  try {
    // Call the loginAndFetchSchedule function from usisController
    const result = await usis.loginAndFetchSchedule( email, password );

    if (result.success) {
      return res.status(200).json(result.schedule);  // Send the fetched schedule as response
    } else {
      return res.status(400).json({ message: result.message });  // Send error message if login failed
    }
  } catch (error) {
    console.error("Error in getSchedule:", error);
    return res.status(500).json({ message: "Internal server error" });
  }
};
