import React, { useState } from 'react';
import UploadImage from './uploadImage';

interface ImagePreviewProps {
  base64String: string;
}

const ImagePreview: React.FC<ImagePreviewProps> = ({ base64String }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleImageClick = () => {
    setIsModalOpen(true); // Open the modal when image is clicked
  };

  const handleCloseModal = () => {
    setIsModalOpen(false); // Close the modal
  };

  return (
    <div className="flex justify-center items-center">
      <div
        className="w-32 h-32 rounded-full overflow-hidden border-2 border-gray-300 flex justify-center items-center cursor-pointer"
        onClick={handleImageClick} // Trigger modal on click
      >
        {base64String !== "" ? (
          <img
            src={`${base64String}`}
            alt="Profile Preview"
            className="w-full h-full object-cover"
          />
        ) : (
          <p className="text-center text-sm text-gray-500">No Image</p>
        )}
      </div>

      {/* Conditionally render the UploadImage modal */}
      {isModalOpen && (
        <UploadImage isOpen={isModalOpen} onClose={handleCloseModal} />
      )}
    </div>
  );
};

export default ImagePreview;
