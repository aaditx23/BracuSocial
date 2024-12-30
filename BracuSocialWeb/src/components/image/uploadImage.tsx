import React, { useState } from "react";
import axios from "axios";

interface UploadImageProps {
  isOpen: boolean;
  onClose: () => void;
}

const UploadImage: React.FC<UploadImageProps> = ({ isOpen, onClose }) => {
  const [_, setImageFile] = useState<File | null>(null);
  const [base64Image, setBase64Image] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  // Function to handle image selection and convert to base64
  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (file.size <= 50 * 1024) {
        // 50KB limit
        setImageFile(file);
        setError(null); // Reset error if file is valid
        convertToBase64(file);
      } else {
        setImageFile(null);
        setError("Cannot upload an image larger than 50KB");
      }
    }
  };

  // Convert the selected image file to base64
  const convertToBase64 = (file: File) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      if (reader.result) {
        setBase64Image(reader.result as string); // Set the base64 string
      }
    };
    reader.readAsDataURL(file); // Convert the file to base64 string
  };

  // Function to handle image upload with base64 string
  const handleUpload = async () => {
    if (!base64Image) return;

    setLoading(true);

    const studentId = localStorage.getItem("id");
    if (!studentId) {
      setError("Student ID is not available.");
      setLoading(false);
      return;
    }

    try {
      // Send base64 string to the backend
      const response = await axios.post(
        "https://bracusocial-web-backend.vercel.app/api/profile/uploadImage",
        {
          studentId,
          profilePicture: base64Image, // Send the base64 string instead of a file
        }
      );
      console.log("Image uploaded successfully:", response.data);
      onClose(); // Close the modal after successful upload
    } catch (error) {
      console.error("Error uploading image:", error);
      setError("Failed to upload image. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null; // Don't render the modal if it's closed

  return (
    <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
      <div className="bg-white p-6 rounded-md w-80">
        <h2 className="text-lg font-semibold text-center">
          Upload Profile Image
        </h2>

        {/* Image input */}
        <div className="mt-4">
          <input
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            className="block w-full text-sm text-gray-700"
          />
        </div>

        {/* Error message */}
        {error && <p className="text-red-600 text-sm mt-2">{error}</p>}

        {/* Display the image preview */}
        {base64Image && (
          <div className="mt-4 flex justify-center">
            <img
              src={base64Image}
              alt="Preview"
              className="max-w-[200px] max-h-[200px] rounded-full object-cover"
            />
          </div>
        )}

        {/* Upload button */}
        <div className="flex justify-center mt-4">
          <button
            onClick={handleUpload}
            disabled={loading || !base64Image}
            className={`px-4 py-2 text-white ${
              loading || !base64Image ? "bg-gray-400" : "bg-blue-500"
            } rounded-md`}
          >
            {loading ? "Uploading..." : "Upload Image"}
          </button>
        </div>

        {/* Close button */}
        <div className="flex justify-center mt-2">
          <button
            onClick={onClose}
            className="px-4 py-2 text-gray-600 border border-gray-300 rounded-md"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default UploadImage;
